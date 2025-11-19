package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.ExternalUrls
import com.chachadev.spotifycmpclone.domain.model.Image
import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val url: String,
    val height: Int? = null,
    val width: Int? = null
) {
    fun toDomain(): Image {
        return Image(
            url = url,
            height = height,
            width = width
        )
    }
}

@Serializable
data class ExternalUrlsDto(
    val spotify: String? = null
) {
    fun toDomain(): ExternalUrls {
        return ExternalUrls(spotify = spotify)
    }
}

