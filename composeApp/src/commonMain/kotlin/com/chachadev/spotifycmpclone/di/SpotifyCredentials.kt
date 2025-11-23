package com.chachadev.spotifycmpclone.di

import com.chachadev.core.network.BuildKonfig

data class SpotifyCredentials(
    val clientId: String = BuildKonfig.CLIENT_ID,
    val clientSecret: String = BuildKonfig.CLIENT_SECRET,
    val redirectUri: String = ""
)

expect object PlatformSpotifyCredentials {
    fun get(): SpotifyCredentials
    fun set(credentials: SpotifyCredentials)
}

