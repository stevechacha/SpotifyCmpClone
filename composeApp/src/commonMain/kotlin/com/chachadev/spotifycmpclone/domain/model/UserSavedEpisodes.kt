package com.chachadev.spotifycmpclone.domain.model

data class UserSavedEpisodes(
    val href: String?,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: String?,
    val total: Int?,
    val items: List<UserSavedEpisode>?
)
