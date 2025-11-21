package com.chachadev.core.common.screen


sealed interface ScreenOrientation

data object Portrait : ScreenOrientation

data object Landscape : ScreenOrientation