package com.chachadev.spotifycmpclone.domain.model

data class SearchResult(
    val tracks: PagingObject<Track>?,
    val albums: PagingObject<Album>?,
    val artists: PagingObject<Artist>?,
    val playlists: PagingObject<Playlist>?
)

data class PagingObject<T>(
    val items: List<T>,
    val total: Int,
    val limit: Int,
    val offset: Int,
    val next: String?,
    val previous: String?
)

