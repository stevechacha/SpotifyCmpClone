package com.chachadev.spotifycmpclone.utils

import androidx.compose.runtime.Composable
import platform.Foundation.NSUserDefaults

private const val AUTH_CODE_KEY = "spotify_auth_code"

@Composable
actual fun getStoredAuthCode(): String? {
    val defaults = NSUserDefaults.standardUserDefaults
    val code = defaults.stringForKey(AUTH_CODE_KEY)
    return code
}

@Composable
actual fun clearStoredAuthCode() {
    val defaults = NSUserDefaults.standardUserDefaults
    defaults.removeObjectForKey(AUTH_CODE_KEY)
    defaults.synchronize()
}

actual fun storeAuthCode(code: String) {
    val defaults = NSUserDefaults.standardUserDefaults
    defaults.setObject(code, AUTH_CODE_KEY)
    defaults.synchronize()
}

