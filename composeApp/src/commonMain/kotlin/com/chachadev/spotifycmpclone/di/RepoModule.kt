package com.chachadev.spotifycmpclone.di

import com.chachadev.spotifycmpclone.data.repository.SpotifyRepositoryImpl
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository
import org.koin.dsl.module

val repoModule = module {
    single<SpotifyRepository> {
        SpotifyRepositoryImpl(
            api = get(),
            authManager = get()
        )
    }
}