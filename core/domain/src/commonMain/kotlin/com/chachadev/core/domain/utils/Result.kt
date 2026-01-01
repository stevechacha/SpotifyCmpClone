package com.chachadev.core.domain.utils

import com.chachadev.core.domain.utils.map

sealed interface Result<out D, out E: com.chachadev.core.domain.utils.Error> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Failure<out E: com.chachadev.core.domain.utils.Error>(val error: E): Result<Nothing, E>
}

inline fun <T, E: com.chachadev.core.domain.utils.Error, R> com.chachadev.core.domain.utils.Result<T, E>.map(map: (T) -> R): com.chachadev.core.domain.utils.Result<R, E> {
    return when(this) {
        is com.chachadev.core.domain.utils.Result.Failure -> _root_ide_package_.com.chachadev.core.domain.utils.Result.Failure(error)
        is com.chachadev.core.domain.utils.Result.Success -> _root_ide_package_.com.chachadev.core.domain.utils.Result.Success(map(this.data))
    }
}

inline fun <T, E: com.chachadev.core.domain.utils.Error> com.chachadev.core.domain.utils.Result<T, E>.onSuccess(action: (T) -> Unit): com.chachadev.core.domain.utils.Result<T, E> {
    return when(this) {
        is com.chachadev.core.domain.utils.Result.Failure -> this
        is com.chachadev.core.domain.utils.Result.Success -> {
            action(this.data)
            this
        }
    }
}

inline fun <T, E: com.chachadev.core.domain.utils.Error> com.chachadev.core.domain.utils.Result<T, E>.onFailure(action: (E) -> Unit): com.chachadev.core.domain.utils.Result<T, E> {
    return when(this) {
        is com.chachadev.core.domain.utils.Result.Failure -> {
            action(error)
            this
        }
        is com.chachadev.core.domain.utils.Result.Success -> this
    }
}

fun <T, E: com.chachadev.core.domain.utils.Error> com.chachadev.core.domain.utils.Result<T, E>.asEmptyResult(): com.chachadev.core.domain.utils.EmptyResult<E> {
    return map {  }
}

typealias EmptyResult<E> = com.chachadev.core.domain.utils.Result<Unit, E>