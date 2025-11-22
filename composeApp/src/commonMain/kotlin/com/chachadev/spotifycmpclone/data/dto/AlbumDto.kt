package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.Album
import com.chachadev.spotifycmpclone.domain.model.Artist
import com.chachadev.spotifycmpclone.domain.model.ExternalUrls
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    val id: String,
    val name: String,
    val artists: List<ArtistDto>,
    val images: List<ImageDto>,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("total_tracks") val totalTracks: Int? = null,
    @SerialName("external_urls") val externalUrls: ExternalUrlsDto? = null,
    val tracks: TracksDto? = null
) {
    fun toDomain(): Album {
        return Album(
            id = id,
            name = name,
            artists = artists.map { it.toDomain() },
            images = images.map { it.toDomain() },
            releaseDate = releaseDate,
            totalTracks = totalTracks,
            externalUrls = externalUrls?.toDomain()
        )
    }
}
/*
data class AlbumDto(
    @SerialName("album_type")
    val albumType: String?,
    @SerialName("total_tracks")
    val totalTracks: Int?,
    @SerialName("available_markets")
    val availableMarkets: List<String>?,
    @SerialName("external_urls")
    val externalUrls: ExternalUrlsDto?,
    val href: String?,
    val id: String?,
    val images: List<ImageDto>?,
    val name: String?,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("release_date_precision")
    val releaseDatePrecision: String?,
    val restrictions: RestrictionsDto?,
    val type: String?,
    val uri: String?,
    val artists: List<ArtistDto>?,
    val tracks: TracksDto?,
    val copyrights: List<CopyrightDto>?,
    @SerialName("external_ids")
    val externalIds: ExternalIDsDto?,
    val genres: List<String>?,
    val label: String?,
    val popularity: Int?
)*/


