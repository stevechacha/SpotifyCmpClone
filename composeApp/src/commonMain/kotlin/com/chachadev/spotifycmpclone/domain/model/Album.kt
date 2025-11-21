package com.chachadev.spotifycmpclone.domain.model

data class Album(
    val id: String,
    val name: String,
    val artists: List<Artist>,
    val images: List<Image>,
    val releaseDate: String?,
    val totalTracks: Int?,
    val externalUrls: ExternalUrls?
)


