package com.chachadev.core.network.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import repo.Resource

fun <T> toResultFlow(call: suspend () -> T?) : Flow<Resource<T?>> {
    return flow<Resource<T?>> {
        emit(Resource.Loading)
        val result = call.invoke()
        result.let { response ->
            try {
                emit(Resource.Success(response))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }
        }
    }.flowOn(Dispatchers.Main)
}






