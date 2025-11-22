package com.chachadev.spotifycmpclone.data.auth

import com.russhwolf.settings.Settings

/**
 * Expect function to provide platform-specific Settings implementation
 */
expect fun createSettings(): Settings

