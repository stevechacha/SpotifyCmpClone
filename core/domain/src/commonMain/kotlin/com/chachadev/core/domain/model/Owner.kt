package com.chachadev.core.domain.model

data class Owner(
    val externalUrls: com.chachadev.core.domain.model.ExternalUrls?,
    val followers: com.chachadev.core.domain.model.Followers?,
    val href: String?,
    val id: String?,
    val type: String?,
    val uri: String?,
    val displayName: String?
)
