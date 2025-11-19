package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
    val id: String,
    val name: String,
    val artists: List<ArtistDto>,
    val album: AlbumDto? = null,
    @SerialName("duration_ms") val durationMs: Int,
    @SerialName("preview_url") val previewUrl: String? = null,
    @SerialName("external_urls") val externalUrls: ExternalUrlsDto? = null,
    val images: List<ImageDto> = emptyList()
) {
    fun toDomain(): Track {
        return Track(
            id = id,
            name = name,
            artists = artists.map { it.toDomain() },
            album = album?.toDomain(),
            durationMs = durationMs,
            previewUrl = previewUrl,
            externalUrls = externalUrls?.toDomain(),
            images = images.map { it.toDomain() }
        )
    }
}

