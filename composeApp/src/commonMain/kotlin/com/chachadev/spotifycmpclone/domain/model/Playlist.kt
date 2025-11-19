package com.chachadev.spotifycmpclone.domain.model

data class Playlist(
    val id: String,
    val name: String,
    val description: String?,
    val images: List<Image>,
    val owner: PlaylistOwner?,
    val tracks: PlaylistTracks?,
    val externalUrls: ExternalUrls?
)

data class PlaylistOwner(
    val id: String,
    val displayName: String?
)

data class PlaylistTracks(
    val total: Int
)

