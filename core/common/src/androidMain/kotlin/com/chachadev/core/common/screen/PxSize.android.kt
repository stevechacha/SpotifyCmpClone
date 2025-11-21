package com.chachadev.core.common.screen


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntSize

// TODO: refactor to use WindowMetricsCalculator.computeCurrentWindowMetrics instead
@Composable
actual fun rememberScreenSizeInPx(): IntSize {
    val configuration = LocalConfiguration.current
    val dpi = configuration.densityDpi
    val width = configuration.screenWidthDp * dpi
    val height = configuration.screenHeightDp * dpi
    return IntSize(width, height)
}