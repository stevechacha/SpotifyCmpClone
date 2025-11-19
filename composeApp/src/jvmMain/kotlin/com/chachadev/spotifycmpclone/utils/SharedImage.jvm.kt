package com.chachadev.spotifycmpclone.utils

import androidx.compose.ui.graphics.ImageBitmap

actual fun ImageBitmap.toBytes(): ByteArray {
    throw UnsupportedOperationException("Not supported on JVM")
}

actual class SharedImage {
    actual fun toByteArray(): ByteArray? = null
    actual fun toImageBitmap(): ImageBitmap? = null
}