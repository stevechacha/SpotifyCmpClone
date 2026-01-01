package com.chachadev.core.domain.model

data class User(
    val id: String,
    val displayName: String?,
    val email: String?,
    val images: List<com.chachadev.core.domain.model.Image>,
    val externalUrls: com.chachadev.core.domain.model.ExternalUrls?,
    val followers: Int?
)




