package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.UserSavedEpisode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSavedEpisodeDto(
    @SerialName("added_at")
    val addedAt: String? = null,
    val episode: EpisodeDto? = null
) {
    fun toDomain(): UserSavedEpisode {
        return UserSavedEpisode(
            addedAt = addedAt,
            episode = episode?.toDomain()
)
    }
}