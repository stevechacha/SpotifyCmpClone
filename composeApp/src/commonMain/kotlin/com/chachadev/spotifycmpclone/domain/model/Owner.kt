package com.chachadev.spotifycmpclone.domain.model

data class Owner(
    val externalUrls: ExternalUrls?,
    val followers: Followers?,
    val href: String?,
    val id: String?,
    val type: String?,
    val uri: String?,
    val displayName: String?
)
