package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image as SkiaImage

@Composable
actual fun rememberImageLoader(): ImageLoader {
    return JvmImageLoader(HttpClient())
}

class JvmImageLoader(private val client: HttpClient) : ImageLoader {
    override suspend fun loadImage(url: String): ImageBitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.get(url)
                val channel = response.bodyAsChannel()
                val bytes = channel.readRemaining().readBytes()
                val image = SkiaImage.makeFromEncoded(bytes)
                val bitmap = Bitmap.makeFromImage(image)
                bitmap.asImageBitmap()
            } catch (e: Exception) {
                null
            }
        }
    }
}
