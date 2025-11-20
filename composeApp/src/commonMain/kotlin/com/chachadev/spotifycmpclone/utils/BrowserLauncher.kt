package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable

@Composable
expect fun rememberBrowserLauncher(): BrowserLauncher

interface BrowserLauncher {
    fun openUrl(url: String)
    fun canHandleUrl(url: String): Boolean
}




