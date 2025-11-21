package com.chachadev.spotifycmpclone.domain.model

data class SpotifyUsersAlbumSaved(
    val href: String?,
    val items: List<SpotifyUsersAlbumSavedItem>,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: String?,
    val total: Int?
)