package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.Episode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeDto(
    @SerialName("audio_preview_url")
    val audioPreviewURL: String?,
    val description: String?,
    @SerialName("html_description")
    val htmlDescription: String?,
    @SerialName("duration_ms")
    val durationMs: Int?,
    val explicit: Boolean?,
    @SerialName("external_urls")
    val externalURLs: ExternalUrlsDto?,
    val href: String?,
    val id: String?,
    val images: List<ImageDto>?,
    @SerialName("is_externally_hosted")
    val isExternallyHosted: Boolean?,
    @SerialName("is_playable")
    val isPlayable: Boolean?,
    val language: String?,
    val languages: List<String>?,
    val name: String?,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("release_date_precision")
    val releaseDatePrecision: String?,
    @SerialName("resume_point")
    val resumePoint: ResumePointDto?,
    val type: String?,
    val uri: String?,
    val restrictions: RestrictionsDto?,
    val show: ShowDto?
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