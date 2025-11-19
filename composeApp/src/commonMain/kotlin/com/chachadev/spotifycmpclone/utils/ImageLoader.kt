package com.chachadev.spotifycmpclone.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
expect fun rememberImageLoader(): ImageLoader

interface ImageLoader {
    suspend fun loadImage(url: String): ImageBitmap?
}

@Composable
fun ImageLoader(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    shape: Shape = RoundedCornerShape(4.dp)
) {
    val imageLoader = rememberImageLoader()
    var imageBitmap by remember(imageUrl) { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember(imageUrl) { mutableStateOf(true) }

    LaunchedEffect(imageUrl) {
        if (imageUrl != null) {
            isLoading = true
            imageBitmap = withContext(Dispatchers.Default) {
                imageLoader.loadImage(imageUrl)
            }
            isLoading = false
        } else {
            imageBitmap = null
            isLoading = false
        }
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            // Could show a loading indicator here
        } else {
            imageBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = contentScale
                )
            }
        }
    }
}
