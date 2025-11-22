package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable
import kotlinx.browser.window

@Composable
actual fun rememberBrowserLauncher(): BrowserLauncher {
    return object : BrowserLauncher {
        override fun openUrl(url: String) {
            // For wasmJs/web, navigate in the same window for OAuth flows
            // This is more reliable than window.open() which can be blocked
            window.location.href = url
        }
        
        override fun canHandleUrl(url: String): Boolean {
            return url.startsWith("http://") || url.startsWith("https://")
        }
    }
}
