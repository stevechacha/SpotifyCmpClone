package com.chachadev.core.domain.model


data class Show(
    val availableMarkets: List<String>?,
    val copyrights: List<com.chachadev.core.domain.model.Copyright>?,
    val description: String?,
    val htmlDescription: String?,
    val explicit: Boolean?,
    val externalUrls: com.chachadev.core.domain.model.ExternalUrls?,
    val href: String?,
    val id: String?,
    val images: List<com.chachadev.core.domain.model.Image>?,
    val isExternallyHosted: Boolean?,
    val languages: List<String>?,
    val mediaType: String?,
    val name: String?,
    val publisher: String?,
    val type: String?,
    val uri: String?,
    val totalEpisodes: Int?
)
