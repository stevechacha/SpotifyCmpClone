package com.chachadev.core.network.dto.spotify

data class ArtistsAlbumDto(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String,
    val total: Int
)

data class Item(
    val album_group: String,
    val album_type: String,
    val artists: List<Artist>,
    val available_markets: List<String>,
    val external_urls: ExternalUrlsX,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val restrictions: Restrictions,
    val total_tracks: Int,
    val type: String,
    val uri: String
)

/*data class Artist(
    val external_urls: ExternalUrlsX,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)*/

data class ExternalUrlsX(
    val spotify: String
)

data class Restrictions(
    val reason: String
)