package com.chachadev.spotifycmpclone.utils


/*
class JsImageLoader(private val client: HttpClient) : ImageLoader {
    override suspend fun loadImage(url: String): ImageBitmap? {
        return withContext(Dispatchers.Default) {
            try {
                val response = client.get(url)
                val channel = response.bodyAsChannel()
                val bytes = channel.readRemaining().readBytes()
                val image = SkiaImage.makeFromEncoded(bytes)
                val bitmap = Bitmap()
                bitmap.allocPixels(image.imageInfo)
                image.readPixels(bitmap)
                bitmap.toComposeImageBitmap()
            } catch (e: Exception) {
                null
            }
        }
    }
}
*/

