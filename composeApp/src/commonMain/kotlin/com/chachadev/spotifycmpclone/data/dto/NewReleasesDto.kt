package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.Album
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewReleasesDto(
    val albums: AlbumsResponseDto
) {
    fun toDomain(): List<Album> {
        return albums.items.map { it.toDomain() }
    }
}

@Serializable
data class AlbumsResponseDto(
    val items: List<AlbumDto>
)

