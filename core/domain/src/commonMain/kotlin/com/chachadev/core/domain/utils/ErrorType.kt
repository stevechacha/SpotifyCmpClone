package com.chachadev.core.domain.utils

sealed class InternetException : Exception() {
    class WifiDisabledException : InternetException()
    class NoInternetException : InternetException()
    class NetworkNotSupportedException : InternetException()
}

open class AppException( message: String): Exception(message)
class UnknownErrorException(message: String) : com.chachadev.core.domain.utils.AppException(message)

class NotFoundException(message: String) : com.chachadev.core.domain.utils.AppException(message)

class SocketClosedException(message: String) : com.chachadev.core.domain.utils.AppException(message)

open class AuthorizationException(val errorMessage: String = "") : com.chachadev.core.domain.utils.AppException(errorMessage) {
     class UnAuthorizedException(message: String) : AuthorizationException(message)
     class InvalidUsernameException(message: String) : AuthorizationException(message)
     class InvalidPasswordException(message: String) : AuthorizationException(message)
     class InvalidFullNameException(message: String) : AuthorizationException(message)
     class InvalidEmailException(message: String) : AuthorizationException(message)
     class InvalidPhoneException(message: String) : AuthorizationException(message)
     class InvalidAddressException(message: String) : AuthorizationException(message)

    class UserNotFoundException(message: String) : AuthorizationException(message)
    class InvalidCredentialsException(message: String) : AuthorizationException(message)
    class UserAlreadyExistException(message: String) : AuthorizationException(message)
    class LocationAccessDeniedException(message: String) : AuthorizationException(message)
}

