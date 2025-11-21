package com.chachadev.core.common.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
actual fun rememberScreenOrientation(): ScreenOrientation {
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp
    val height = configuration.screenHeightDp
    return if (width > height) Landscape else Portrait
}