package com.chachadev.core.domain.utils

class DataErrorException(
    val error: DataError
): Exception()