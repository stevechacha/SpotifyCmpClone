package com.chachadev.core.network.datasource

import com.chachadev.core.network.api.NetworkApi
import com.chachadev.core.network.dto.ApiResponse
import com.chachadev.core.network.dto.UserDetailsDto
import com.chachadev.core.network.dto.UserRegistrationDto

interface RemoteDataSource {
    suspend fun getProducts(): ApiResponse
    suspend fun createUser(userRegistrationDto: UserRegistrationDto): UserDetailsDto
}


