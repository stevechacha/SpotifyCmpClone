package com.chachadev.core.network.api

import api.IUserApiService
import com.chachadev.core.network.utils.BaseHttpClient
import com.chachadev.core.network.dto.ServerResponse
import com.chachadev.core.network.dto.UserDetailsDto
import com.chachadev.core.network.dto.UserRegistrationDto
import com.chachadev.core.network.mapper.toDomain
import com.chachadev.core.network.mapper.toUserRegistrationDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.utils.io.InternalAPI
import kotlinx.serialization.json.Json
import model.Account
import model.User
import utils.AuthorizationException

class UserApiService(client: HttpClient) : BaseHttpClient(client), IUserApiService {

    @OptIn(InternalAPI::class)
    override suspend fun createUser(account: Account): User {
        return tryToExecute<ServerResponse<UserDetailsDto>> {
            post("/signup") {
                val userRegistrationDto = account.toUserRegistrationDto()
                body = Json.encodeToString(UserRegistrationDto.serializer(), userRegistrationDto)
            }
        }.value?.toDomain()
            ?: throw AuthorizationException.InvalidCredentialsException("Invalid Credential")
    }


}