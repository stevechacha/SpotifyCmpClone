package com.chachadev.spotifycmpclone.domain.model

data class Artist(
    val id: String,
    val name: String,
    val images: List<Image> = emptyList(),
    val externalUrls: ExternalUrls?,
    val genres: List<String> = emptyList(),
    val followers: Int? = null
)



