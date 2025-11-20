package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable
import java.awt.Desktop

@Composable
actual fun rememberBrowserLauncher(): BrowserLauncher {
    return object : BrowserLauncher {
        override fun openUrl(url: String) {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(java.net.URI(url))
            }
        }
        
        override fun canHandleUrl(url: String): Boolean {
            return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
        }
    }
}




