package com.chachadev.spotifycmpclone.di

import java.util.Properties

actual object PlatformSpotifyCredentials {
    private val credentials: SpotifyCredentials by lazy { loadCredentials() }

    actual fun get(): SpotifyCredentials = credentials

    actual fun set(credentials: SpotifyCredentials) {
        // No-op on JVM desktop; credentials are loaded from resources.
        // This function exists to satisfy the expect/actual contract.
    }

    private fun loadCredentials(): SpotifyCredentials {
        return runCatching {
            val props = Properties()
            val stream = PlatformSpotifyCredentials::class.java.classLoader
                ?.getResourceAsStream("spotify.properties")
            if (stream != null) {
                stream.use { props.load(it) }
                SpotifyCredentials(
                    clientId = props.getProperty("SpotifyClientID", ""),
                    clientSecret = props.getProperty("SpotifyClientSecret", ""),
                    redirectUri = props.getProperty("SpotifyRedirectURI", "")
                )
            } else {
                SpotifyCredentials()
            }
        }.getOrElse { SpotifyCredentials() }
    }
}

