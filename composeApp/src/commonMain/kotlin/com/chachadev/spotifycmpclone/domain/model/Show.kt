package com.chachadev.spotifycmpclone.domain.model


data class Show(
    val availableMarkets: List<String>?,
    val copyrights: List<Copyright>?,
    val description: String?,
    val htmlDescription: String?,
    val explicit: Boolean?,
    val externalUrls: ExternalUrls?,
    val href: String?,
    val id: String?,
    val images: List<Image>?,
    val isExternallyHosted: Boolean?,
    val languages: List<String>?,
    val mediaType: String?,
    val name: String?,
    val publisher: String?,
    val type: String?,
    val uri: String?,
    val totalEpisodes: Int?
)
