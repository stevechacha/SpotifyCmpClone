package com.chachadev.spotifycmpclone.utils

import kotlinx.browser.window

actual fun getStoredAuthCode(): String? {
    return window.sessionStorage.getItem("spotify_auth_code")
}

actual fun clearStoredAuthCode() {
    window.sessionStorage.removeItem("spotify_auth_code")
}




