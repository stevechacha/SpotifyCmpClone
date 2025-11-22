package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.SpotifyUsersAlbumSavedItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyUsersAlbumSavedItemDto(
    @SerialName("added_at")
    val addedAt: String,
    val album: AlbumDto?
) {
    fun toDomain(): SpotifyUsersAlbumSavedItem {
        return SpotifyUsersAlbumSavedItem(
            addedAt = addedAt,
            album = album?.toDomain()
)
    }
}