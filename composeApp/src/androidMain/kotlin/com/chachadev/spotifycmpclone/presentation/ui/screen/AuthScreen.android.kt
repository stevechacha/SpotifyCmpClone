package com.chachadev.spotifycmpclone.presentation.ui.screen

actual fun getStoredAuthCode(): String? {
    // For Android, this would be handled via deep links or custom URL scheme
    // For now, return null - will be implemented with deep link handling
    return null
}

actual fun clearStoredAuthCode() {
    // No-op for Android
}




