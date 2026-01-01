package com.chachadev.core.domain.model

data class Tracks(
    val href: String?,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: String?,
    val total: Int?,
    val items: List<com.chachadev.core.domain.model.Track>?
)