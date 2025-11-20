package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable
import kotlinx.browser.window

@Composable
actual fun rememberBrowserLauncher(): BrowserLauncher {
    return object : BrowserLauncher {
        override fun openUrl(url: String) {
            window.open(url, "_blank", "noopener,noreferrer")
        }
        
        override fun canHandleUrl(url: String): Boolean {
            return url.startsWith("http://") || url.startsWith("https://")
        }
    }
}




