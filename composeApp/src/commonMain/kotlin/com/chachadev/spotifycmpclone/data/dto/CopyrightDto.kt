package com.chachadev.spotifycmpclone.data.dto

import com.chachadev.spotifycmpclone.domain.model.Copyright
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