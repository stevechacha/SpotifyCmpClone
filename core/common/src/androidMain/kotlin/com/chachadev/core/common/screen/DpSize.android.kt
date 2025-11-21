package com.chachadev.core.common.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

// TODO: refactor to use WindowMetricsCalculator.computeCurrentWindowMetrics instead
@Composable
actual fun rememberScreenSizeInDp(): DpSize {
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp.dp
    val height = configuration.screenHeightDp.dp
    return DpSize(width, height)
}
