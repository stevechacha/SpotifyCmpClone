package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable
import kotlinx.browser.window

const val AUTH_CODE_KEY = "spotify_auth_code"

@Composable
actual fun getStoredAuthCode(): String? {
    return window.localStorage.getItem(AUTH_CODE_KEY)
}

@Composable
actual fun clearStoredAuthCode() {
    window.localStorage.removeItem(AUTH_CODE_KEY)
}
