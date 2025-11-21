package com.chachadev.spotifycmpclone.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExternalIDsDto(
    val isrc: String?,
    val ean: String?,
    val upc: String?
)

