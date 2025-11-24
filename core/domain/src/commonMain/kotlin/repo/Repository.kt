package repo

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.Account
import model.DomainModel
import model.User

interface Repository {
    suspend fun getProducts(): Flow<Resource<DomainModel>>
    suspend fun createUser(account: Account): User
}

sealed class Resource<out T> {

    data class Success<T>(val data: T?) : Resource<T>()

    data class Error<T>(val message: String) : Resource<T>()

    object Loading : Resource<Nothing>()

}

