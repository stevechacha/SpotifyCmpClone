package com.chachadev.spotifycmpclone.di

import com.chachadev.core.database.auth.SettingsManager
import com.chachadev.core.network.BuildKonfig
import com.chachadev.core.network.datasource.SpotifyRemoteDataSource
import com.chachadev.core.network.datasource.SpotifyRemoteDataSourceImpl
import com.chachadev.core.data.auth.AuthManager
import com.chachadev.spotifycmpclone.data.auth.createSettings
import com.chachadev.core.network.factory.HttpClientFactory
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val apiModule = module {
    single { HttpClientFactory.create() }
    
    // Provide Settings instance for multiplatform-settings
    // Uses platform-specific implementation via expect/actual
    single<Settings> { 
        createSettings()
    }
    
    // Provide SettingsManager
    single {
        SettingsManager(
            settings = get()
        )
    }

    single {
        AuthManager(
            httpClient = get(),
            clientId = BuildKonfig.CLIENT_ID ,//getProperty("spotify_client_id", ""),
            clientSecret = BuildKonfig.CLIENT_SECRET, //getProperty("spotify_client_secret", ""),
            redirectUri = getProperty("spotify_redirect_uri", ""),
            settingsManager = get()
        )
    }

    // Provide SpotifyRemoteDataSource
    single<SpotifyRemoteDataSource> {
        SpotifyRemoteDataSourceImpl(
            client = get(),
            baseUrl = "https://api.spotify.com/v1"
        )
    }
}