package com.chachadev.spotifycmpclone

import android.app.Application
import com.chachadev.spotifycmpclone.data.auth.initSettingsContext
import com.chachadev.spotifycmpclone.di.PlatformSpotifyCredentials
import com.chachadev.spotifycmpclone.di.SpotifyCredentials
import com.chachadev.spotifycmpclone.utils.initAuthCodeStorage

class SpotifyCmpCloneApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize settings context for multiplatform-settings
        initSettingsContext(this)
        
        // Initialize auth code storage with application context
        initAuthCodeStorage(this)
        
        val clientId = getString(R.string.spotify_client_id)
        val clientSecret = getString(R.string.spotify_client_secret)
        val redirectUri = getString(R.string.spotify_redirect_uri)
        PlatformSpotifyCredentials.set(
            SpotifyCredentials(
                clientId = clientId,
                clientSecret = clientSecret,
                redirectUri = redirectUri
            )
        )
    }

}