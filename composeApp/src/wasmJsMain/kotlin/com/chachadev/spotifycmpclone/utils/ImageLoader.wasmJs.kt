package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

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