package com.chachadev.spotifycmpclone.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private const val PREFS_NAME = "spotify_auth_prefs"
private const val AUTH_CODE_KEY = "spotify_auth_code"

// Global Application context - initialized in Application.onCreate()
private var appContext: Context? = null

fun initAuthCodeStorage(context: Context) {
    appContext = context.applicationContext
}

@Composable
actual fun getStoredAuthCode(): String? {
    val context = LocalContext.current
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getString(AUTH_CODE_KEY, null)
}

@Composable
actual fun clearStoredAuthCode() {
    val context = LocalContext.current
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().remove(AUTH_CODE_KEY).apply()
}

actual fun storeAuthCode(code: String) {
    // Get Application context - initialized in Application.onCreate()
    val context = appContext ?: throw IllegalStateException(
        "AuthCode storage not initialized. Make sure initAuthCodeStorage() is called in Application.onCreate()"
    )
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putString(AUTH_CODE_KEY, code).apply()
}

// Android-specific function to store auth code with context (used from MainActivity)
fun storeAuthCode(context: Context, code: String) {
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putString(AUTH_CODE_KEY, code).apply()
}

// Android-specific function to clear auth code with context (can be called from non-composable contexts)
fun clearStoredAuthCode(context: Context) {
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().remove(AUTH_CODE_KEY).apply()
}

