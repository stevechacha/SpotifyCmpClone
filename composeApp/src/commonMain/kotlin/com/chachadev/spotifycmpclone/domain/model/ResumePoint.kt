package com.chachadev.spotifycmpclone.domain.model

import kotlinx.serialization.SerialName

data class ResumePoint(
    val fullyPlayed: Boolean?,
    val resumePositionMS: Int?
)
