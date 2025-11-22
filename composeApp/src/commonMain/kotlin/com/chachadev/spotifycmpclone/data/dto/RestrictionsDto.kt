package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.Restrictions
import kotlinx.serialization.Serializable

@Serializable
data class RestrictionsDto(
    val reason: String? = null
) {
    fun toDomain(): Restrictions {
        return Restrictions(
            reason = reason
)
    }
}