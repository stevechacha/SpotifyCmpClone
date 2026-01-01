package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.Copyright
import kotlinx.serialization.Serializable

@Serializable
data class CopyrightDto(
    val text: String?,
    val type: String?
) {
    fun toDomain(): Copyright {
        return Copyright(
            text = text,
            type = type
)
    }
}