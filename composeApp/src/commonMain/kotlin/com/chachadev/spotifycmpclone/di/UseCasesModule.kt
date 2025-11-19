package com.chachadev.spotifycmpclone.di

import com.chachadev.spotifycmpclone.domain.usecase.GetAlbumDetailsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetAlbumTracksUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetArtistDetailsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetArtistTopTracksUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetFeaturedPlaylistsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetNewReleasesUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetPlaylistDetailsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetPlaylistTracksUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetTrackDetailsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.SearchUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::SearchUseCase)
    factoryOf(::GetNewReleasesUseCase)
    factoryOf(::GetFeaturedPlaylistsUseCase)
    factoryOf(::GetArtistTopTracksUseCase)
    factoryOf(::GetAlbumDetailsUseCase)
    factoryOf(::GetAlbumTracksUseCase)
    factoryOf(::GetPlaylistDetailsUseCase)
    factoryOf(::GetPlaylistTracksUseCase)
    factoryOf(::GetArtistDetailsUseCase)
    factoryOf(::GetTrackDetailsUseCase)
}