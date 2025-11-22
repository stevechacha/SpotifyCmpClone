package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.Episode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeDto(
    @SerialName("audio_preview_url")
    val audioPreviewURL: String? = null,
    val description: String? = null,
    @SerialName("html_description")
    val htmlDescription: String? = null,
    @SerialName("duration_ms")
    val durationMs: Int? = null,
    val explicit: Boolean? = null,
    @SerialName("external_urls")
    val externalURLs: ExternalUrlsDto? = null,
    val href: String? = null,
    val id: String? = null,
    val images: List<ImageDto>? = null,
    @SerialName("is_externally_hosted")
    val isExternallyHosted: Boolean? = null,
    @SerialName("is_playable")
    val isPlayable: Boolean? = null,
    val language: String? = null,
    val languages: List<String>? = null,
    val name: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("release_date_precision")
    val releaseDatePrecision: String? = null,
    @SerialName("resume_point")
    val resumePoint: ResumePointDto? = null,
    val type: String? = null,
    val uri: String? = null,
    val restrictions: RestrictionsDto? = null,
    val show: ShowDto? = null
) {
    fun toDomain(): Episode {
        return Episode(
            audioPreviewURL = audioPreviewURL,
            description = description,
            htmlDescription = htmlDescription,
            durationMs = durationMs,
            explicit = explicit,
            externalURLs = externalURLs?.toDomain(),
            href = href,
            id = id,
            images = images?.map { it.toDomain() },
            isExternallyHosted = isExternallyHosted,
            isPlayable = isPlayable,
            language = language,
            languages = languages,
            name = name,
            releaseDate = releaseDate,
            releaseDatePrecision = releaseDatePrecision,
            resumePoint = resumePoint?.toDomain(),
            type = type,
            uri = uri,
            restrictions = restrictions?.toDomain(),
            show = show?.toDomain()
        )
    }
}