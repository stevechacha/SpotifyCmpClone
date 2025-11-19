package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import com.chachadev.spotifycmpclone.domain.model.Art


actual val Art.artUrl: String
    get() = this.headerImage?.url ?: this.webImage.url

actual val minGridSize: Int
    get() = 325

@Composable
actual fun rememberImageLoader(): ImageLoader {
    return WasmImageLoader
}

private object WasmImageLoader : ImageLoader {
    override suspend fun loadImage(url: String): ImageBitmap? {
        // TODO: Implement once image loading support is available for wasm target.
        return null
    }
}