package com.chachadev.spotifycmpclone.presentation.ui.screen

import kotlinx.browser.window

actual fun getStoredAuthCode(): String? {
    return window.sessionStorage.getItem("spotify_auth_code")
}

actual fun clearStoredAuthCode() {
    window.sessionStorage.removeItem("spotify_auth_code")
}




