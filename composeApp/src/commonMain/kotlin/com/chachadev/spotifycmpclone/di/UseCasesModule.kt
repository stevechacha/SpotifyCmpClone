package com.chachadev.spotifycmpclone.di

import com.chachadev.spotifycmpclone.domain.usecase.GetAlbumDetailsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetAlbumTracksUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetArtistDetailsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetArtistTopTracksUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetCurrentUserPlaylistsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetFeaturedPlaylistsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetNewReleasesUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetPlaylistDetailsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetPlaylistTracksUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetRecentlyPlayedTracksUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetTrackDetailsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetUserSavedAlbumsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetUserSavedEpisodesUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetUserSavedShowsUseCase
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
    factoryOf(::GetCurrentUserPlaylistsUseCase)
    factoryOf(::GetUserSavedAlbumsUseCase)
    factoryOf(::GetUserSavedShowsUseCase)
    factoryOf(::GetUserSavedEpisodesUseCase)
    factoryOf(::GetRecentlyPlayedTracksUseCase)
}