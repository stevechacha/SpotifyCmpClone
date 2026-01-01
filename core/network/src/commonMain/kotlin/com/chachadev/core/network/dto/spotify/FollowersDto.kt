package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.Artist
import com.chachadev.core.domain.model.Followers
import kotlinx.serialization.Serializable

@Serializable
data class FollowersDto(
    val href: String? = null,
    val total: Int? = null
){
    fun toDomain(): Followers {
        return Followers(
            href = href,
            total = total
        )
    }

}
