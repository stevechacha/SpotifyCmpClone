package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import spotifycmpclone.composeapp.generated.resources.Res
import spotifycmpclone.composeapp.generated.resources.compose_multiplatform

@Composable
fun rememberCustomImagePainter(
    model: Any?,
    imageLoader: ImageLoader,
    contentScale: ContentScale = ContentScale.Fit,
) = rememberAsyncImagePainter(
    model, imageLoader,
    error = painterResource(Res.drawable.compose_multiplatform),
    placeholder = painterResource(Res.drawable.compose_multiplatform),
    contentScale = contentScale
)

@Composable
fun rememberCustomImagePainter(
    model: Any?,
    contentScale: ContentScale = ContentScale.Fit,
) = rememberAsyncImagePainter(
    model = model,
    error = painterResource(Res.drawable.compose_multiplatform),
    placeholder = painterResource(Res.drawable.compose_multiplatform),
    contentScale = contentScale,
)