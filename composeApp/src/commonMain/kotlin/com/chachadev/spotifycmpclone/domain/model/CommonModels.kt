package com.chachadev.spotifycmpclone.domain.model

data class Image(
    val url: String,
    val height: Int?,
    val width: Int?
)

data class ExternalUrls(
    val spotify: String?
)

