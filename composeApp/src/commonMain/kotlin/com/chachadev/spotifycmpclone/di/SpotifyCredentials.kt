package com.chachadev.spotifycmpclone.di

data class SpotifyCredentials(
    val clientId: String = "",
    val clientSecret: String = "",
    val redirectUri: String = ""
)

expect object PlatformSpotifyCredentials {
    fun get(): SpotifyCredentials
    fun set(credentials: SpotifyCredentials)
}

