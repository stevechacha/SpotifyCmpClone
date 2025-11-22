package com.chachadev.spotifycmpclone

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.chachadev.core.common.config.toWindowState
import com.chachadev.spotifycmpclone.deeplink.DesktopDeepLinkHandler
import com.chachadev.spotifycmpclone.presentation.navigation.ExternalUriHandler
import com.chachadev.spotifycmpclone.utils.configuration

fun main(args: Array<String>) {
    DesktopDeepLinkHandler.setup()

    val initialDeepLink = args.firstOrNull {
        val cleanedDeepLink = it.trim('"')

        DesktopDeepLinkHandler.supportedUriPatterns.any { it.matches(cleanedDeepLink) }
    }?.trim('"')

    application {

        val config = configuration()

        var canReceiveDeepLink by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(canReceiveDeepLink) {
            if(canReceiveDeepLink && initialDeepLink != null) {
                ExternalUriHandler.onNewUri(initialDeepLink)
            }
        }


        Window(
            onCloseRequest = ::exitApplication,
            title = "SpotifyCmpClone",
            state = config.toWindowState(),
            focusable = true,
        ) {
            App()
        }
    }
}