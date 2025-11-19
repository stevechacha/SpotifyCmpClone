package com.chachadev.spotifycmpclone.presentation.ui.screen

actual fun getStoredAuthCode(): String? {
    // For desktop, this could be handled via local server or file
    // For now, return null
    return null
}

actual fun clearStoredAuthCode() {
    // No-op for desktop
}

