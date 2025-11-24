package repo

import com.chachadev.core.network.datasource.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.chachadev.core.network.mapper.toDomain
import com.chachadev.core.network.mapper.toUserRegistrationDto
import model.Account
import model.DomainModel
import model.User

class RepositoryImpl(private val remoteDataSource: RemoteDataSource) : Repository {
    override suspend fun getProducts(): Flow<Resource<DomainModel>> {
        return flow {
            emit(Resource.Loading)
            try {
                // Get ApiResponse from remote data source
                val apiResponse = remoteDataSource.getProducts()
                // Map ApiResponse to DomainModel using the mapper
                val domainModel = apiResponse.toDomain()
                emit(Resource.Success(domainModel))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }
        }.flowOn(Dispatchers.Main)
    }

    override suspend fun createUser(account: Account): User {
        val apiResponse = remoteDataSource.createUser(account.toUserRegistrationDto())
        return apiResponse.toDomain()
    }
}