package com.chachadev.core.network.dto.spotify

import kotlinx.serialization.Serializable

@Serializable
data class ExternalIDsDto(
    val isrc: String?,
    val ean: String?,
    val upc: String?
)

