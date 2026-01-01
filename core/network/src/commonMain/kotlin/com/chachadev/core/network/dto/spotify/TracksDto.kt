package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.Tracks
import kotlinx.serialization.Serializable

@Serializable
data class TracksDto(
    val href: String? = null,
    val limit: Int? = null,
    val next: String? = null,
    val offset: Int? = null,
    val previous: String? = null,
    val total: Int? = null,
    val items: List<TrackDto>? = null
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

