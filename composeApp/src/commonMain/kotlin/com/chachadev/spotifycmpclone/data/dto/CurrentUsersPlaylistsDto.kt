package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.CurrentUsersPlaylists
import kotlinx.serialization.Serializable

@Serializable
data class CurrentUsersPlaylistsDto(
    val href: String? = null,
    val limit: Int? = null,
    val next: String? = null,
    val offset: Int? = null,
    val previous: String? = null,
    val total: Int? = null,
    val items: List<PlaylistItemDto>? = null
){
    fun toDomain(): CurrentUsersPlaylists {
        return CurrentUsersPlaylists(
            href = href,
            limit = limit,
            next = next,
            offset = offset,
            previous = previous,
            total = total,
            items = items?.map { it.toDomain() }
        )
    }

}

