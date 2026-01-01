package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.Show
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowDto(
    @SerialName("available_markets")
    val availableMarkets: List<String>?,
    val copyrights: List<CopyrightDto>?,
    val description: String?,
    @SerialName("html_description")
    val htmlDescription: String?,
    val explicit: Boolean?,
    @SerialName("external_urls")
    val externalUrls: ExternalUrlsDto?,
    val href: String?,
    val id: String?,
    val images: List<ImageDto>?,
    @SerialName("is_externally_hosted")
    val isExternallyHosted: Boolean?,
    val languages: List<String>?,
    @SerialName("media_type")
    val mediaType: String?,
    val name: String?,
    val publisher: String?,
    val type: String?,
    val uri: String?,
    @SerialName("total_episodes")
    val totalEpisodes: Int?
) {
    fun toDomain(): Show {
        return Show(
            availableMarkets = availableMarkets,
            copyrights = copyrights?.map { it.toDomain() },
            description = description,
            htmlDescription = htmlDescription,
            explicit = explicit,
            externalUrls = externalUrls?.toDomain(),
            href = href,
            id = id,
            images = images?.map { it.toDomain() },
            isExternallyHosted = isExternallyHosted,
            languages = languages,
            mediaType = mediaType,
            name = name,
            publisher = publisher,
            type = type,
            uri = uri,
            totalEpisodes = totalEpisodes
        )
    }
}
