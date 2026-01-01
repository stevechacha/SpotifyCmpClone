package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.UsersSavedShowsItems
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsersSavedShowsItemsDto(
    @SerialName("added_at")
    val addedAt: String,
    val show: ShowDto
) {
    fun toDomain(): UsersSavedShowsItems {
        return UsersSavedShowsItems(
            addedAt = addedAt,
            show = show.toDomain()
)
    }
}