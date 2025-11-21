package com.chachadev.spotifycmpclone

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.chachadev.core.common.config.toWindowState
import com.chachadev.spotifycmpclone.utils.configuration

fun main() = application {

    val config = configuration()

    Window(
        onCloseRequest = ::exitApplication,
        title = "SpotifyCmpClone",
        state = config.toWindowState(),
        focusable = true,
    ) {
        App()
    }
}