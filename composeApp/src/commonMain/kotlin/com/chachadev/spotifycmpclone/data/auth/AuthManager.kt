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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class AuthManager(
    private val httpClient: HttpClient,
    private val clientId: String,
    private val clientSecret: String
) {

    private val mutex = Mutex()
    private var cachedToken: String? = null
    private var expiresAtMillis: Long = 0L

    @OptIn(ExperimentalTime::class)
    suspend fun getValidToken(): String = mutex.withLock {
        val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
        val token = cachedToken
        if (token != null && now < expiresAtMillis - REFRESH_MARGIN_MS) {
            return token
        }

        val response = requestToken()
        cachedToken = response.accessToken
        expiresAtMillis = now + (response.expiresIn - SAFETY_SECONDS).coerceAtLeast(30).seconds.inWholeMilliseconds
        response.accessToken
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

    companion object {
        private const val TOKEN_URL = "https://accounts.spotify.com/api/token"
        private const val SAFETY_SECONDS = 60
        private const val REFRESH_MARGIN_MS = 30_000L
    }
}

