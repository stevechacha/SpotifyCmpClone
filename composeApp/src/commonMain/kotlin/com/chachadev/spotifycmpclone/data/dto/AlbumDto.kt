package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.Album
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    val id: String,
    val name: String,
    val artists: List<ArtistDto>,
    val images: List<ImageDto>,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("total_tracks") val totalTracks: Int? = null,
    @SerialName("external_urls") val externalUrls: ExternalUrlsDto? = null
) {
    fun toDomain(): Album {
        return Album(
            id = id,
            name = name,
            artists = artists.map { it.toDomain() },
            images = images.map { it.toDomain() },
            releaseDate = releaseDate,
            totalTracks = totalTracks,
            externalUrls = externalUrls?.toDomain()
        )
    }
}

