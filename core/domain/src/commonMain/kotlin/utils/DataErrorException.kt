package utils

class DataErrorException(
    val error: DataError
): Exception()