package com.chachadev.core.network.utils

import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.*
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import utils.DataError
import utils.Result

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return try {
            val response = execute()
            handleResponse(response)
        } catch(e: UnresolvedAddressException) {
            Result.Failure(DataError.Remote.NO_INTERNET)
        } catch(e: HttpRequestTimeoutException) {
            Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
        } catch(e: SerializationException) {
            Result.Failure(DataError.Remote.SERIALIZATION)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            // Check for network-related errors in WASM/JS
            val errorMessage = e.message?.lowercase() ?: ""
            when {
                errorMessage.contains("network") || 
                errorMessage.contains("failed to fetch") || 
                errorMessage.contains("networkerror") -> {
                    Result.Failure(DataError.Remote.NO_INTERNET)
                }
                errorMessage.contains("timeout") -> {
                    Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
                }
                else -> Result.Failure(DataError.Remote.UNKNOWN)
            }

    }
}
