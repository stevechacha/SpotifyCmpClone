package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.UsersSavedShows
import kotlinx.serialization.Serializable

@Serializable
data class UsersSavedShowsDto(
    val href: String = "",
    val limit: Int = 0,
    val next: String? = null,
    val offset: Int = 0,
    val previous: String? = null,
    val total: Int? = null,
    val items: List<UsersSavedShowsItemsDto>? = null
) {
    fun toDomain(): UsersSavedShows {
        return UsersSavedShows(
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