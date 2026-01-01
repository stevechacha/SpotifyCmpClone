package com.chachadev.core.domain.model

data class Art(
    val objectNumber: String,
    val title: String,
    val longTitle: String,
    val webImage: com.chachadev.core.domain.model.WebImage,
    val headerImage: com.chachadev.core.domain.model.HeaderImage?,
    val productionPlaces: List<String>
)