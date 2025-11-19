package com.chachadev.spotifycmpclone.utils

import androidx.compose.ui.graphics.ImageBitmap

expect fun ImageBitmap.toBytes(): ByteArray

expect class SharedImage {
    fun toByteArray(): ByteArray?
    fun toImageBitmap(): ImageBitmap?
}
