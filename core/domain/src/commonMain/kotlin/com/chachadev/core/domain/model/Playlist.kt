package com.chachadev.core.domain.model

data class Playlist(
    val id: String,
    val name: String,
    val description: String?,
    val images: List<com.chachadev.core.domain.model.Image>,
    val owner: com.chachadev.core.domain.model.PlaylistOwner?,
    val tracks: com.chachadev.core.domain.model.PlaylistTracks?,
    val externalUrls: com.chachadev.core.domain.model.ExternalUrls?
)

data class PlaylistOwner(
    val id: String,
    val displayName: String?
)

data class PlaylistTracks(
    val total: Int
)

