package com.chachadev.core.domain.model

data class SearchResult(
    val tracks: com.chachadev.core.domain.model.PagingObject<com.chachadev.core.domain.model.Track>?,
    val albums: com.chachadev.core.domain.model.PagingObject<com.chachadev.core.domain.model.Album>?,
    val artists: com.chachadev.core.domain.model.PagingObject<com.chachadev.core.domain.model.Artist>?,
    val playlists: com.chachadev.core.domain.model.PagingObject<com.chachadev.core.domain.model.Playlist>?
)

data class PagingObject<T>(
    val items: List<T>,
    val total: Int,
    val limit: Int,
    val offset: Int,
    val next: String?,
    val previous: String?
)

