package com.chachadev.core.domain.model

data class Album(
    val id: String,
    val name: String,
    val artists: List<com.chachadev.core.domain.model.Artist>,
    val images: List<com.chachadev.core.domain.model.Image>,
    val releaseDate: String?,
    val totalTracks: Int?,
    val externalUrls: com.chachadev.core.domain.model.ExternalUrls?
)


