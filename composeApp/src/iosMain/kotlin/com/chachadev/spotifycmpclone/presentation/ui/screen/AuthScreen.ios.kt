package com.chachadev.spotifycmpclone.presentation.ui.screen

actual fun getStoredAuthCode(): String? {
    // For iOS, this would be handled via URL scheme callback
    // For now, return null - will be implemented with URL scheme handling
    return null
}

actual fun clearStoredAuthCode() {
    // No-op for iOS
}

