package com.chachadev.spotifycmpclone

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.chachadev.spotifycmpclone.di.PlatformSpotifyCredentials
import com.chachadev.spotifycmpclone.di.SpotifyCredentials
import platform.Foundation.NSBundle

private var credentialsInitialized = false

fun MainViewController() = ComposeUIViewController {
    if (!credentialsInitialized) {
        PlatformSpotifyCredentials.set(
            SpotifyCredentials(
                clientId = infoValue("SpotifyClientID"),
                clientSecret = infoValue("SpotifyClientSecret"),
                redirectUri = infoValue("SpotifyRedirectURI")
            )
        )
        credentialsInitialized = true
    }
    App()
}

private fun infoValue(key: String): String {
    val dictionary = NSBundle.mainBundle.infoDictionary
    return dictionary?.get(key) as? String ?: ""
}