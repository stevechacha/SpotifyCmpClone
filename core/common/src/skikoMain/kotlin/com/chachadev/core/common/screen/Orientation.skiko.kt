package com.chachadev.core.common.screen


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
actual fun rememberScreenOrientation(): ScreenOrientation {
    val window = LocalWindowInfo.current.containerSize
    return if (window.width > window.height) Landscape else Portrait
}