package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    @SerialName("display_name") val displayName: String? = null,
    val email: String? = null,
    val images: List<ImageDto> = emptyList(),
    @SerialName("external_urls") val externalUrls: ExternalUrlsDto? = null,
    val followers: FollowersDto? = null
) {
    fun toDomain(): User {
        return User(
            id = id,
            displayName = displayName,
            email = email,
            images = images.map { it.toDomain() },
            externalUrls = externalUrls?.toDomain(),
            followers = followers?.total
        )
    }
}




