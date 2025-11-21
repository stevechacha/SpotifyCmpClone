package com.chachadev.spotifycmpclone.di

import com.chachadev.spotifycmpclone.data.api.LibraryApiService
import com.chachadev.spotifycmpclone.data.api.SpotifyApi
import com.chachadev.spotifycmpclone.data.auth.AuthManager
import com.chachadev.spotifycmpclone.data.network.HttpClientFactory
import org.koin.dsl.module

val apiModule = module {
    single { HttpClientFactory.create() }

    single {
        AuthManager(
            httpClient = get(),
            clientId = getProperty("spotify_client_id", ""),
            clientSecret = getProperty("spotify_client_secret", ""),
            redirectUri = getProperty("spotify_redirect_uri", "")
        )
    }

    single {
        SpotifyApi(
            client = get(),
            baseUrl = "https://api.spotify.com/v1"
        )
    }

    single {
        LibraryApiService(
            client = get(),
            baseUrl = "https://api.spotify.com/v1"
        )
    }
}