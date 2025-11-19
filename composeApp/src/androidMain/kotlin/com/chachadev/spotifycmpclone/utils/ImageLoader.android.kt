package com.chachadev.spotifycmpclone.utils

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.core.readBytes

@Composable
actual fun rememberImageLoader(): ImageLoader {
    return AndroidImageLoader(HttpClient())
}

class AndroidImageLoader(private val client: HttpClient) : ImageLoader {
    override suspend fun loadImage(url: String): ImageBitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.get(url)
                val channel = response.bodyAsChannel()
                val bytes = channel.readRemaining().readBytes()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                bitmap?.asImageBitmap()
            } catch (e: Exception) {
                null
            }
        }
    }
}

