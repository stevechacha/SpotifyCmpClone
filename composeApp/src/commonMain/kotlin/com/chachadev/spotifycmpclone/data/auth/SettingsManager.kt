package com.chachadev.spotifycmpclone.data.auth

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

/**
 * Manages persistent storage for authentication state using multiplatform-settings.
 * Works across Android, iOS, Desktop, and Web.
 */
class SettingsManager(private val settings: Settings) {
    
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_EXPIRES_AT_MILLIS = "expires_at_millis"
        private const val KEY_IS_SIGNED_IN = "is_signed_in"
    }
    
    // Access Token
    fun getAccessToken(): String? {
        val value = settings.getString(KEY_ACCESS_TOKEN, "")
        return if (value.isNotEmpty()) value else null
    }
    fun setAccessToken(token: String?) {
        if (token != null) {
            settings[KEY_ACCESS_TOKEN] = token
        } else {
            settings.remove(KEY_ACCESS_TOKEN)
        }
    }
    
    // Refresh Token
    fun getRefreshToken(): String? {
        val value = settings.getString(KEY_REFRESH_TOKEN, "")
        return if (value.isNotEmpty()) value else null
    }
    fun setRefreshToken(token: String?) {
        if (token != null) {
            settings[KEY_REFRESH_TOKEN] = token
        } else {
            settings.remove(KEY_REFRESH_TOKEN)
        }
    }
    
    // Expires At
    fun getExpiresAtMillis(): Long = settings.getLong(KEY_EXPIRES_AT_MILLIS, 0L)
    fun setExpiresAtMillis(millis: Long) {
        settings[KEY_EXPIRES_AT_MILLIS] = millis
    }
    
    // Signed In State
    fun getIsSignedIn(): Boolean = settings.getBoolean(KEY_IS_SIGNED_IN, false)
    fun setIsSignedIn(signedIn: Boolean) {
        settings[KEY_IS_SIGNED_IN] = signedIn
    }
    
    // Clear all auth data
    fun clearAuthData() {
        setAccessToken(null)
        setRefreshToken(null)
        setExpiresAtMillis(0L)
        setIsSignedIn(false)
    }
}


