package com.chachadev.core.domain.model

data class Episode(
    val audioPreviewURL: String?,
    val description: String?,
    val htmlDescription: String?,
    val durationMs: Int?,
    val explicit: Boolean?,
    val externalURLs: com.chachadev.core.domain.model.ExternalUrls?,
    val href: String?,
    val id: String?,
    val images: List<com.chachadev.core.domain.model.Image>?,
    val isExternallyHosted: Boolean?,
    val isPlayable: Boolean?,
    val language: String?,
    val languages: List<String>?,
    val name: String?,
    val releaseDate: String?,
    val releaseDatePrecision: String?,
    val resumePoint: com.chachadev.core.domain.model.ResumePoint?,
    val type: String?,
    val uri: String?,
    val restrictions: com.chachadev.core.domain.model.Restrictions?,
    val show: com.chachadev.core.domain.model.Show?
)