package com.chachadev.core.domain.utils

import com.chachadev.core.domain.utils.onFailure
import com.chachadev.core.domain.utils.onSuccess
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

class Paginator<Key, Item>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> com.chachadev.core.domain.utils.Result<List<Item>, com.chachadev.core.domain.utils.DataError>,
    private val getNextKey: suspend (List<Item>) -> Key,
    private val onError: suspend (Throwable?) -> Unit,
    private val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
) {
    private var currentKey = initialKey
    private var isMakingRequest = false
    private var lastRequestKey: Key? = null

    suspend fun loadNextItems() {
        if(isMakingRequest) {
            return
        }

        if(currentKey != null && currentKey == lastRequestKey) {
            return
        }

        isMakingRequest = true
        onLoadUpdated(true)

        try {
            onRequest(currentKey)
                .onSuccess { items ->
                    val newKey = getNextKey(items)
                    onSuccess(items, newKey)
                    lastRequestKey = currentKey

                    currentKey = newKey
                }
                .onFailure { error ->
                    onError(
                        _root_ide_package_.com.chachadev.core.domain.utils.DataErrorException(
                            error
                        )
                    )
                }
        } catch(e: Exception) {
            coroutineContext.ensureActive()

            onError(e)
        } finally {
            onLoadUpdated(false)
            isMakingRequest = false
        }
    }

    fun reset() {
        currentKey = initialKey
        lastRequestKey = null
    }
}