package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.Tracks
import kotlinx.serialization.Serializable

@Serializable
data class TracksDto(
    val href: String?,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: String?,
    val total: Int?,
    val items: List<TrackDto>?
) {
    fun toDomain(): Tracks {
        return Tracks(
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

