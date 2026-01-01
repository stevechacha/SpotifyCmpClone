package com.chachadev.core.domain.model

data class Address(
    val id: String,
    val address: String,
    val location: com.chachadev.core.domain.model.Location? = null,
)