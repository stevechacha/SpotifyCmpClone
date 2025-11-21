package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.Artist
import com.chachadev.spotifycmpclone.domain.model.ExternalUrls
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    val id: String,
    val name: String,
    val images: List<ImageDto> = emptyList(),
    @SerialName("external_urls") val externalUrls: ExternalUrlsDto? = null,
    val genres: List<String> = emptyList(),
    val followers: FollowersDto? = null
) {
    fun toDomain(): Artist {
        return Artist(
            id = id,
            name = name,
            images = images.map { it.toDomain() },
            externalUrls = externalUrls?.toDomain(),
            genres = genres,
            followers = followers?.total
        )
    }
}

/*data class ArtistDto(
    val externalUrls: ExternalUrls?,
    val href: String?,
    val id: String?,
    val name: String?,
    val type: String?,
    val uri: String?
)*/


