package com.chachadev.core.common.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntSize

@Composable
actual fun rememberScreenSizeInPx(): IntSize = LocalWindowInfo.current.containerSize