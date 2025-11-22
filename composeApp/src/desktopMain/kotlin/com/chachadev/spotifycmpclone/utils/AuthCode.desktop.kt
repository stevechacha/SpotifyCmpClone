package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable

@Composable
actual fun getStoredAuthCode(): String? {
    // For desktop, this would be handled via deep links or custom URL scheme
    // For now, return null - will be implemented with deep link handling
    return null
}

@Composable
actual fun clearStoredAuthCode() {
    // No-op for desktop
}

actual fun storeAuthCode(code: String) {
    // For desktop, this would be handled via deep links or custom URL scheme
    // For now, no-op - will be implemented with deep link handling
}
