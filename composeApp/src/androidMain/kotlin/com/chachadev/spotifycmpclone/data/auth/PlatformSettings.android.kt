package com.chachadev.spotifycmpclone.data.auth

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual fun createSettings(): Settings {
    // Get application context - we'll need to inject this
    val context = getApplicationContext()
    val prefs = context.getSharedPreferences("spotify_auth", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(prefs)
}

// We need a way to get the application context
// This will be initialized in the app
private var appContext: Context? = null

fun initSettingsContext(context: Context) {
    appContext = context.applicationContext
}

private fun getApplicationContext(): Context {
    return appContext ?: throw IllegalStateException("Application context not initialized. Call initSettingsContext() first.")
}

