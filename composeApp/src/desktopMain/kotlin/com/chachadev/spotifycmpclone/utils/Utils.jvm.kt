package com.chachadev.spotifycmpclone.utils

import com.chachadev.core.domain.model.Art

actual val Art.artUrl: String
    get() = this.headerImage?.url ?: this.webImage.url

actual val minGridSize: Int
    get() = 325