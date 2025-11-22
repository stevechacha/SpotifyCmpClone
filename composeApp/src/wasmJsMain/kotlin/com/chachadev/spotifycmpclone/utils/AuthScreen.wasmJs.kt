package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable
import kotlinx.browser.window

private const val AUTH_CODE_KEY = "spotify_auth_code"

@Composable
actual fun getStoredAuthCode(): String? {
    return window.localStorage.getItem(AUTH_CODE_KEY)
}

@Composable
actual fun clearStoredAuthCode() {
    window.localStorage.removeItem(AUTH_CODE_KEY)
}

// wasmJs-specific function to store auth code (used from callback handler)
fun storeAuthCode(code: String) {
    window.localStorage.setItem(AUTH_CODE_KEY, code)
}
