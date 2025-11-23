package com.chachadev.spotifycmpclone.data.auth

import com.chachadev.spotifycmpclone.utils.encodeUrlComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.scope.Scope
import kotlin.getValue
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class AuthManager(
    private val httpClient: HttpClient,
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String,
    private val settingsManager: SettingsManager
) {

    private val mutex = Mutex()
    private var cachedToken: String? = null
    private var expiresAtMillis: Long = 0L
    private var refreshToken: String? = null
    
    private val _isSignedIn = MutableStateFlow(false)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn.asStateFlow()
    
    init {
        // Load persisted state on initialization
        loadPersistedState()
    }
    
    private fun loadPersistedState() {
        cachedToken = settingsManager.getAccessToken()
        refreshToken = settingsManager.getRefreshToken()
        expiresAtMillis = settingsManager.getExpiresAtMillis()
        val persistedSignedIn = settingsManager.getIsSignedIn()
        _isSignedIn.value = persistedSignedIn
        
        println("AuthManager: Loaded persisted state - signedIn: $persistedSignedIn, hasToken: ${cachedToken != null}, hasRefreshToken: ${refreshToken != null}")
    }
    
    fun setSignedIn(signedIn: Boolean) {
        _isSignedIn.value = signedIn
        settingsManager.setIsSignedIn(signedIn)
        
        // Clear auth data if signing out
        if (!signedIn) {
            clearAuthData()
        }
    }
    
    private fun clearAuthData() {
        cachedToken = null
        refreshToken = null
        expiresAtMillis = 0L
        settingsManager.clearAuthData()
        println("AuthManager: Cleared all auth data")
    }
    
    fun getRedirectUri(): String {
        return redirectUri
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
                
                // Persist updated tokens
                settingsManager.setAccessToken(refreshResponse.accessToken)
                refreshResponse.refreshToken?.let { rt -> settingsManager.setRefreshToken(rt) }
                settingsManager.setExpiresAtMillis(expiresAtMillis)
                
                return refreshResponse.accessToken
            } else {
                // Refresh token failed - user needs to sign in again
                println("AuthManager: Token refresh failed - signing out user")
                setSignedIn(false)
                throw Exception("Token refresh failed. Please sign in again.")
            }
        }

        // If we were signed in but have no refresh token, sign out
        if (_isSignedIn.value) {
            println("AuthManager: No refresh token available but user is signed in - signing out")
            setSignedIn(false)
            throw Exception("Session expired. Please sign in again.")
        }

        // Fallback to client credentials only if user is not signed in
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
            
            println("AuthManager: Exchanging code for token...")
            println("AuthManager: Redirect URI: $redirectUri")
            
            val httpResponse = httpClient.post(TOKEN_URL) {
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
            }
            
            println("AuthManager: HTTP Status: ${httpResponse.status}")
            
            // Check if response is successful
            if (!httpResponse.status.isSuccess()) {
                val errorBody = httpResponse.bodyAsText()
                println("AuthManager: Error response body: $errorBody")
                return Result.failure(Exception("Token exchange failed: ${httpResponse.status} - $errorBody"))
            }
            
            val responseBody = httpResponse.bodyAsText()
            println("AuthManager: Response body: $responseBody")
            
            // Check if response is an error (Spotify returns error responses as JSON with "error" field)
            if (responseBody.contains("\"error\"")) {
                try {
                    val errorResponse = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
                        .decodeFromString<OAuthErrorResponse>(responseBody)
                    println("AuthManager: OAuth error: ${errorResponse.error} - ${errorResponse.errorDescription}")
                    return Result.failure(Exception("OAuth error: ${errorResponse.error} - ${errorResponse.errorDescription}"))
                } catch (e: Exception) {
                    println("AuthManager: Failed to parse error response: ${e.message}")
                }
            }
            
            val response = httpResponse.body<OAuthTokenResponse>()
            
            println("AuthManager: Token exchange successful")
            println("AuthManager: Token type: ${response.tokenType}")
            println("AuthManager: Expires in: ${response.expiresIn}")
            println("AuthManager: Has refresh token: ${response.refreshToken != null}")
            println("AuthManager: Scope: ${response.scope}")
            
            // Cache the tokens
            mutex.withLock {
                cachedToken = response.accessToken
                refreshToken = response.refreshToken
                val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
                expiresAtMillis = now + (response.expiresIn - SAFETY_SECONDS).coerceAtLeast(30).seconds.inWholeMilliseconds
                
                // Persist tokens
                settingsManager.setAccessToken(response.accessToken)
                settingsManager.setRefreshToken(response.refreshToken)
                settingsManager.setExpiresAtMillis(expiresAtMillis)
            }
            
            setSignedIn(true)
            Result.success(response)
        } catch (e: Exception) {
            println("AuthManager: Exception during token exchange: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    @OptIn(ExperimentalTime::class, ExperimentalEncodingApi::class)
    private suspend fun refreshAccessToken(refreshToken: String): OAuthTokenResponse? {
        return try {
            val basic = Base64.encode("$clientId:$clientSecret".encodeToByteArray())
            
            val httpResponse = httpClient.post(TOKEN_URL) {
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
            }
            
            if (!httpResponse.status.isSuccess()) {
                val errorBody = httpResponse.bodyAsText()
                println("AuthManager: Token refresh failed - HTTP ${httpResponse.status}: $errorBody")
                return null
            }
            
            val response = httpResponse.body<OAuthTokenResponse>()
            this.refreshToken = response.refreshToken ?: refreshToken
            println("AuthManager: Token refreshed successfully")
            response
        } catch (e: Exception) {
            println("AuthManager: Exception during token refresh: ${e.message}")
            e.printStackTrace()
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
    
    @Serializable
    data class OAuthErrorResponse(
        val error: String,
        @SerialName("error_description") val errorDescription: String? = null
    )

    companion object {
        private const val TOKEN_URL = "https://accounts.spotify.com/api/token"
        private const val SAFETY_SECONDS = 60
        private const val REFRESH_MARGIN_MS = 30_000L
    }
}


fun Scope.authorizationIntercept(client: HttpClient) {

    val localConfigurationGateway: SettingsManager by inject()
    val userRemoteGateway: AuthManager by inject()

    client.plugin(HttpSend).intercept { request ->

        val accessToken = localConfigurationGateway.getAccessToken()

        request.headers {
            append("Authorization", "Bearer $accessToken")
        }

        val originalCall = execute(request)
        if (originalCall.response.status.value == 401 && accessToken?.isNotEmpty() == true) {
            val access = userRemoteGateway.getValidToken()
            val refresh = localConfigurationGateway.getRefreshToken()
            localConfigurationGateway.setAccessToken(access)
            refresh?.let { localConfigurationGateway.setRefreshToken(it) }
            execute(request)
        } else {
            originalCall
        }
    }
}

