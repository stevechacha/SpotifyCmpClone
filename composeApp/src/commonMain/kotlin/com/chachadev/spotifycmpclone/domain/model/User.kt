package com.chachadev.spotifycmpclone.domain.model

data class User(
    val id: String,
    val displayName: String?,
    val email: String?,
    val images: List<Image>,
    val externalUrls: ExternalUrls?,
    val followers: Int?
)




