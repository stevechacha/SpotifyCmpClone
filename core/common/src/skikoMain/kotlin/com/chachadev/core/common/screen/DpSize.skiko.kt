package com.chachadev.core.common.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize

@Composable
actual fun rememberScreenSizeInDp(): DpSize {
    val density = LocalDensity.current
    val size = LocalWindowInfo.current.containerSize
    return with(density) { DpSize(size.width.toDp(), size.height.toDp()) }
}