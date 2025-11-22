package com.chachadev.spotifycmpclone.data.auth

import com.russhwolf.settings.Settings
import com.russhwolf.settings.PreferencesSettings
import java.util.prefs.Preferences

actual fun createSettings(): Settings {
    // Use Java Preferences API for desktop (works on JVM)
    val preferences = Preferences.userRoot().node("com.chachadev.spotifycmpclone")
    return PreferencesSettings(preferences)
}

