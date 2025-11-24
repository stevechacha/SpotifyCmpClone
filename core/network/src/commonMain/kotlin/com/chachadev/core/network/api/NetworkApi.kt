package com.chachadev.core.network.api

import com.chachadev.core.network.utils.NetworkingConfig
import com.chachadev.core.network.dto.ApiResponse
import com.chachadev.core.network.dto.UserDetailsDto
import com.chachadev.core.network.dto.UserRegistrationDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class NetworkApi(private val client: HttpClient) {
    val products = "products?limit=100&skip=5"
    suspend fun getProducts(): ApiResponse {
       return client.get("${NetworkingConfig.BASE_URL}$products").body<ApiResponse>()
    }

    suspend fun createUser(userRegistrationDto: UserRegistrationDto): UserDetailsDto {
        return client.post("${NetworkingConfig.BASE_URL}/signup") {
            setBody(userRegistrationDto)               // send JSON body
            contentType(ContentType.Application.Json)  // ensure JSON content type
        }.body()
    }
}
