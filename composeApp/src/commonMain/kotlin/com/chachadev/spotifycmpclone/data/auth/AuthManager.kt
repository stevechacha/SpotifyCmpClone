package com.chachadev.spotifycmpclone.data.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.parameters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.chachadev.spotifycmpclone.utils.encodeUrlComponent
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class AuthManager(
    private val httpClient: HttpClient,
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String
) {

    private val mutex = Mutex()
    private var cachedToken: String? = null
    private var expiresAtMillis: Long = 0L
    private var refreshToken: String? = null
    
    private val _isSignedIn = MutableStateFlow(false)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn.asStateFlow()
    
    fun setSignedIn(signedIn: Boolean) {
        _isSignedIn.value = signedIn
    }
    
    fun getSignInUrl(): String {
        val scopes = listOf(
            "user-read-private",
            "user-read-email",
            "playlist-read-private",
            "playlist-modify-public",
            "user-read-recently-played",
            "user-read-playback-state",
            "user-modify-playback-state",
            "user-read-currently-playing",
            "user-library-modify",
            "user-library-read",
            "user-top-read"
        ).joinToString(" ")
        
        val baseUrl = "https://accounts.spotify.com/authorize"
        val encodedRedirectUri = encodeUrlComponent(redirectUri)
        val encodedScopes = encodeUrlComponent(scopes)
        
        val params = listOf(
            "response_type=code",
            "client_id=$clientId",
            "scope=$encodedScopes",
            "redirect_uri=$encodedRedirectUri",
            "show_dialog=TRUE"
        ).joinToString("&")
        
        return "$baseUrl?$params"
    }

    @OptIn(ExperimentalTime::class)
    suspend fun getValidToken(): String = mutex.withLock {
        val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
        val token = cachedToken
        if (token != null && now < expiresAtMillis - REFRESH_MARGIN_MS) {
            return token
        }

        // Try to refresh token if we have one
        refreshToken?.let {
            val refreshResponse = refreshAccessToken(it)
            if (refreshResponse != null) {
                cachedToken = refreshResponse.accessToken
                refreshResponse.refreshToken?.let { rt -> refreshToken = rt }
                expiresAtMillis = now + (refreshResponse.expiresIn - SAFETY_SECONDS).coerceAtLeast(30).seconds.inWholeMilliseconds
                return refreshResponse.accessToken
            }
        }

        // Fallback to client credentials if no refresh token
        val response = requestToken()
        cachedToken = response.accessToken
        expiresAtMillis = now + (response.expiresIn - SAFETY_SECONDS).coerceAtLeast(30).seconds.inWholeMilliseconds
        response.accessToken
    }
    
    @OptIn(ExperimentalTime::class)
    suspend fun exchangeCodeForToken(code: String): Result<OAuthTokenResponse> {
        return try {
            @OptIn(ExperimentalEncodingApi::class)
            val basic = Base64.encode("$clientId:$clientSecret".encodeToByteArray())
            
            val response = httpClient.post(TOKEN_URL) {
                header(HttpHeaders.Authorization, "Basic $basic")
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(
                    FormDataContent(
                        parameters {
                            append("grant_type", "authorization_code")
                            append("code", code)
                            append("redirect_uri", redirectUri)
                        }
                    )
                )
            }.body<OAuthTokenResponse>()
            
            // Cache the tokens
            mutex.withLock {
                cachedToken = response.accessToken
                refreshToken = response.refreshToken
                val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
                expiresAtMillis = now + (response.expiresIn - SAFETY_SECONDS).coerceAtLeast(30).seconds.inWholeMilliseconds
            }
            
            setSignedIn(true)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    @OptIn(ExperimentalTime::class, ExperimentalEncodingApi::class)
    private suspend fun refreshAccessToken(refreshToken: String): OAuthTokenResponse? {
        return try {
            val basic = Base64.encode("$clientId:$clientSecret".encodeToByteArray())
            
            val response = httpClient.post(TOKEN_URL) {
                header(HttpHeaders.Authorization, "Basic $basic")
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(
                    FormDataContent(
                        parameters {
                            append("grant_type", "refresh_token")
                            append("refresh_token", refreshToken)
                        }
                    )
                )
            }.body<OAuthTokenResponse>()
            
            this.refreshToken = response.refreshToken ?: refreshToken
            response
        } catch (e: Exception) {
            null
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private suspend fun requestToken(): TokenResponse {
        require(clientId.isNotBlank()) { "Spotify clientId is missing. Please provide it via initKoin." }
        require(clientSecret.isNotBlank()) { "Spotify clientSecret is missing. Please provide it via initKoin." }

        val basic = Base64.encode("$clientId:$clientSecret".encodeToByteArray())

        return httpClient.post(TOKEN_URL) {
            header(HttpHeaders.Authorization, "Basic $basic")
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                FormDataContent(
                    parameters {
                        append("grant_type", "client_credentials")
                    }
                )
            )
        }.body()
    }

    @Serializable
    private data class TokenResponse(
        @SerialName("access_token") val accessToken: String,
        @SerialName("expires_in") val expiresIn: Long,
        @SerialName("token_type") val tokenType: String
    )
    
    @Serializable
    data class OAuthTokenResponse(
        @SerialName("access_token") val accessToken: String,
        @SerialName("expires_in") val expiresIn: Long,
        @SerialName("refresh_token") val refreshToken: String? = null,
        @SerialName("scope") val scope: String? = null,
        @SerialName("token_type") val tokenType: String
    )

    companion object {
        private const val TOKEN_URL = "https://accounts.spotify.com/api/token"
        private const val SAFETY_SECONDS = 60
        private const val REFRESH_MARGIN_MS = 30_000L
    }
}

