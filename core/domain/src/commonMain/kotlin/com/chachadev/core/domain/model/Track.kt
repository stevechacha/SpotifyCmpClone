package com.chachadev.core.domain.model


data class Track(
    val id: String,
    val name: String,
    val artists: List<Artist>,
    val album: Album?,
    val durationMs: Int,
    val previewUrl: String?,
    val externalUrls: ExternalUrls?,
    val images: List<Image> = emptyList()
)