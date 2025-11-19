package com.chachadev.spotifycmpclone

import android.app.Application
import com.chachadev.spotifycmpclone.di.PlatformSpotifyCredentials
import com.chachadev.spotifycmpclone.di.SpotifyCredentials

class SpotifyCmpCloneApp : Application() {
    override fun onCreate() {
        super.onCreate()
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