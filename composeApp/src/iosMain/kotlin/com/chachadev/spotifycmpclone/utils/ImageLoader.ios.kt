package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.readByteArray

@Composable
actual fun rememberImageLoader(): ImageLoader {
    return IOSImageLoader(HttpClient())
}

class IOSImageLoader(private val client: HttpClient) : ImageLoader {
    override suspend fun loadImage(url: String): ImageBitmap? {
        return withContext(Dispatchers.Default) {
            try {
                val response = client.get(url)
                val channel = response.bodyAsChannel()
                val bytes = channel.readRemaining().readByteArray()
//                val nsData = NSData.create(bytes = bytes)
//                val uiImage = UIImage.imageWithData(nsData)
                // For now, return null as UIImage to ImageBitmap conversion requires platform-specific code
                // In production, you'd convert UIImage to ImageBitmap
                null
            } catch (e: Exception) {
                null
            }
        }
    }
}

