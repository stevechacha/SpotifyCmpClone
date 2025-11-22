package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.Artist
import com.chachadev.spotifycmpclone.domain.model.Followers
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
