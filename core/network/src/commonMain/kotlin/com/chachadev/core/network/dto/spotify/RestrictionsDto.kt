package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.Restrictions
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