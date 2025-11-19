package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable
import platform.UIKit.UIApplication
import platform.Foundation.NSURL

@Composable
actual fun rememberBrowserLauncher(): BrowserLauncher {
    return object : BrowserLauncher {
        override fun openUrl(url: String) {
            val nsUrl = NSURL.URLWithString(url)
            nsUrl?.let {
                UIApplication.sharedApplication.openURL(it)
            }
        }
        
        override fun canHandleUrl(url: String): Boolean {
            val nsUrl = NSURL.URLWithString(url)
            return nsUrl != null && UIApplication.sharedApplication.canOpenURL(nsUrl)
        }
    }
}

