package com.chachadev.core.network.dto.spotify

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistsResponseDto(
    val message: String? = null,
    val playlists: PlaylistsPagingDto? = null
)

@Serializable
data class PlaylistsPagingDto(
    val items: List<PlaylistDto>
)

@Serializable
data class TracksResponseDto(
    val tracks: List<TrackDto>? = null,
    val items: List<TrackDto>? = null
)

@Serializable
data class PlaylistTrackItemDto(
    val track: TrackDto
)

@Serializable
data class PlaylistTracksResponseDto(
    val items: List<PlaylistTrackItemDto>
)

@Serializable
data class EpisodesResponseDto(
    val items: List<EpisodeDto>? = null,
    val limit: Int? = null,
    val offset: Int? = null,
    val total: Int? = null,
    val next: String? = null,
    val previous: String? = null
)


