package com.chachadev.core.network.datasource

import com.chachadev.core.network.api.NetworkApi
import com.chachadev.core.network.dto.ApiResponse
import com.chachadev.core.network.dto.UserDetailsDto
import com.chachadev.core.network.dto.UserRegistrationDto

class RemoteDataSourceImpl(
    private val apiService: NetworkApi
) : RemoteDataSource {

    override suspend fun getProducts(): ApiResponse {
        return apiService.getProducts()
    }

    override suspend fun createUser(
        userRegistrationDto: UserRegistrationDto
    ): UserDetailsDto {
        return apiService.createUser(userRegistrationDto)
    }
}