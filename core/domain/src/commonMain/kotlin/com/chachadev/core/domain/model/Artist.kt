package com.chachadev.core.domain.model

data class Artist(
    val id: String,
    val name: String,
    val images: List<com.chachadev.core.domain.model.Image> = emptyList(),
    val externalUrls: com.chachadev.core.domain.model.ExternalUrls?,
    val genres: List<String> = emptyList(),
    val followers: Int? = null
)



