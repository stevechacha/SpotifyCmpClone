package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.Album
import com.chachadev.spotifycmpclone.domain.model.Artist
import com.chachadev.spotifycmpclone.domain.model.PagingObject
import com.chachadev.spotifycmpclone.domain.model.Playlist
import com.chachadev.spotifycmpclone.domain.model.SearchResult
import com.chachadev.spotifycmpclone.domain.model.Track
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultDto(
    val tracks: PagingObjectDto<TrackDto>? = null,
    val albums: PagingObjectDto<AlbumDto>? = null,
    val artists: PagingObjectDto<ArtistDto>? = null,
    val playlists: PagingObjectDto<PlaylistDto>? = null
) {
    fun toDomain(): SearchResult {
        return SearchResult(
            tracks = tracks?.toDomain { it.toDomain() },
            albums = albums?.toDomain { it.toDomain() },
            artists = artists?.toDomain { it.toDomain() },
            playlists = playlists?.toDomain { it.toDomain() }
        )
    }
}

@Serializable
data class PagingObjectDto<T>(
    val items: List<T?> = emptyList(),
    val total: Int = 0,
    val limit: Int = 0,
    val offset: Int = 0,
    val next: String? = null,
    val previous: String? = null
) {
    fun <R> toDomain(mapper: (T) -> R): PagingObject<R> {
        return PagingObject(
            items = items.mapNotNull { item -> item?.let(mapper) },
            total = total,
            limit = limit,
            offset = offset,
            next = next,
            previous = previous
        )
    }
}

