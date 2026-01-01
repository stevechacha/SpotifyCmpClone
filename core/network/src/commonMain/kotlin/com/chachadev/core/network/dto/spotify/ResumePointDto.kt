package com.chachadev.core.network.dto.spotify

import com.chachadev.core.domain.model.ResumePoint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResumePointDto(
    @SerialName("fully_played")
    val fullyPlayed: Boolean? = null,
    @SerialName("resume_position_ms")
    val resumePositionMS: Int? = null
) {
    fun toDomain(): ResumePoint {
        return ResumePoint(
            fullyPlayed = fullyPlayed,
            resumePositionMS = resumePositionMS
)
    }
}