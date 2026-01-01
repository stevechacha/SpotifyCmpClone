package com.chachadev.core.domain.model

data class SpotifyUsersAlbumSaved(
    val href: String?,
    val items: List<com.chachadev.core.domain.model.SpotifyUsersAlbumSavedItem>,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: String?,
    val total: Int?
)