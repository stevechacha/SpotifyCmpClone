package com.chachadev.core.network.utils

import com.chachadev.core.network.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(engine: HttpClientEngine = getClientEngine()): HttpClient {
        return HttpClient(engine) {
            defaultRequest {
                url(NetworkingConfig.BASE_URL)
                contentType(ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = false
                    }
                )
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = NetworkingConfig.TIMEOUT_SECONDS * 1000
                connectTimeoutMillis = NetworkingConfig.TIMEOUT_SECONDS * 1000
            }
        }
    }

    fun createAuthentication(engine: HttpClientEngine = getClientEngine(),getToken: () -> String): HttpClient {
        return HttpClient(engine) {
            defaultRequest {
                url(NetworkingConfig.BASE_URL)
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer ${getToken()}")
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = false
                    }
                )
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
                logger = object : Logger{
                    override fun log(message: String) {
                        println("HTTP Client: $message")
                    }
                }
            }

            install(HttpTimeout){
                requestTimeoutMillis = NetworkingConfig.TIMEOUT_SECONDS * 1000
                connectTimeoutMillis = NetworkingConfig.TIMEOUT_SECONDS * 1000
                socketTimeoutMillis = NetworkingConfig.TIMEOUT_SECONDS * 1000
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(getToken(), "")
                    }
                }
            }

            install(DefaultRequest){
                header("Authorization", "Bearer ${getToken()}")
            }
        }
    }
}


object NetworkingConfig {
    const val BASE_URL = ""
    const val TIMEOUT_SECONDS = 30L
    val CLIENT_ID = BuildKonfig.CLIENT_ID
    val CLIENT_SECRET = BuildKonfig.CLIENT_SECRET
}