package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.UserSavedEpisodes
import kotlinx.serialization.Serializable

@Serializable
data class UserSavedEpisodesDto(
    val href: String? = null,
    val limit: Int? = null,
    val next: String? = null,
    val offset: Int? = null,
    val previous: String? = null,
    val total: Int? = null,
    val items: List<UserSavedEpisodeDto>? = null
) {
    fun toDomain(): UserSavedEpisodes {
        return UserSavedEpisodes(
            href = href,
            limit = limit,
            next = next,
            offset = offset,
            previous = previous,
            total = total,
            items = items?.map { it.toDomain() }
        )
    }
}
