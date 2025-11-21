package com.chachadev.spotifycmpclone.domain.model

data class Episode(
    val audioPreviewURL: String?,
    val description: String?,
    val htmlDescription: String?,
    val durationMs: Int?,
    val explicit: Boolean?,
    val externalURLs: ExternalUrls?,
    val href: String?,
    val id: String?,
    val images: List<Image>?,
    val isExternallyHosted: Boolean?,
    val isPlayable: Boolean?,
    val language: String?,
    val languages: List<String>?,
    val name: String?,
    val releaseDate: String?,
    val releaseDatePrecision: String?,
    val resumePoint: ResumePoint?,
    val type: String?,
    val uri: String?,
    val restrictions: Restrictions?,
    val show: Show?
)