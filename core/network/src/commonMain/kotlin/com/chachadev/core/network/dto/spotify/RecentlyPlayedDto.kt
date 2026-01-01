package com.chachadev.core.network.dto.spotify

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecentlyPlayedResponseDto(
    val href: String? = null,
    val limit: Int? = null,
    val next: String? = null,
    val cursors: CursorsDto? = null,
    val total: Int? = null,
    val items: List<RecentlyPlayedItemDto>? = null
)

@Serializable
data class RecentlyPlayedItemDto(
    val track: TrackDto? = null,
    @SerialName("played_at") val playedAt: String? = null,
    val context: ContextDto? = null
)

@Serializable
data class CursorsDto(
    val after: String? = null,
    val before: String? = null
)

@Serializable
data class ContextDto(
    val type: String? = null,
    val href: String? = null,
    @SerialName("external_urls") val externalUrls: ExternalUrlsDto? = null,
    val uri: String? = null
)



