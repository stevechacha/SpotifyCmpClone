package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.SpotifyUsersAlbumSavedItem
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