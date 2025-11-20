package com.chachadev.spotifycmpclone.window

import androidx.compose.ui.window.WindowState

data class ApplicationState(
    val windows: List<WindowState> = listOf(WindowState()),
)