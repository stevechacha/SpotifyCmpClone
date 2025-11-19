package com.chachadev.spotifycmpclone.di

actual object PlatformSpotifyCredentials {
    private var credentials: SpotifyCredentials = SpotifyCredentials()

    actual fun get(): SpotifyCredentials = credentials

    actual fun set(credentials: SpotifyCredentials) {
        this.credentials = credentials
    }
}

