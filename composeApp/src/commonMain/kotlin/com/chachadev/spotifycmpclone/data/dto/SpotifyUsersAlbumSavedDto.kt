package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.SpotifyUsersAlbumSaved
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyUsersAlbumSavedDto(
    val href: String? = null,
    val items: List<SpotifyUsersAlbumSavedItemDto> = emptyList(),
    val limit: Int? = null,
    val next: String? = null,
    val offset: Int? = null,
    val previous: String? = null,
    val total: Int? = null
) {
    fun toDomain(): SpotifyUsersAlbumSaved {
        return SpotifyUsersAlbumSaved(
            href = href,
            items = items.map { it.toDomain() },
            limit = limit,
            next = next,
            offset = offset,
            previous = previous,
            total = total
        )
    }
}