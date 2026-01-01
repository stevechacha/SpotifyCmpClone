package com.chachadev.spotifycmpclone.di

import com.chachadev.core.data.repository.LibraryRepositoryImpl
import com.chachadev.core.data.repository.SpotifyRepositoryImpl
import com.chachadev.core.domain.repository.LibraryRepository
import com.chachadev.core.domain.repository.SpotifyRepository
import org.koin.dsl.module

val repoModule = module {
    single<SpotifyRepository> {
        SpotifyRepositoryImpl(
            remoteDataSource = get(),
            authProvider = get<com.chachadev.core.data.auth.AuthManager>()
        )
    }

    single<LibraryRepository> {
        LibraryRepositoryImpl(
            remoteDataSource = get(),
            authProvider = get<com.chachadev.core.data.auth.AuthManager>()
        )
    }
}