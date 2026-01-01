package com.chachadev.spotifycmpclone.di

import com.chachadev.core.domain.usecase.GetAlbumDetailsUseCase
import com.chachadev.core.domain.usecase.GetAlbumTracksUseCase
import com.chachadev.core.domain.usecase.GetArtistDetailsUseCase
import com.chachadev.core.domain.usecase.GetArtistTopTracksUseCase
import com.chachadev.core.domain.usecase.GetCurrentUserPlaylistsUseCase
import com.chachadev.core.domain.usecase.GetFeaturedPlaylistsUseCase
import com.chachadev.core.domain.usecase.GetNewReleasesUseCase
import com.chachadev.core.domain.usecase.GetPlaylistDetailsUseCase
import com.chachadev.core.domain.usecase.GetPlaylistTracksUseCase
import com.chachadev.core.domain.usecase.GetRecentlyPlayedTracksUseCase
import com.chachadev.core.domain.usecase.GetShowDetailsUseCase
import com.chachadev.core.domain.usecase.GetShowEpisodesUseCase
import com.chachadev.core.domain.usecase.GetTrackDetailsUseCase
import com.chachadev.core.domain.usecase.GetUserSavedAlbumsUseCase
import com.chachadev.core.domain.usecase.GetUserSavedEpisodesUseCase
import com.chachadev.core.domain.usecase.GetUserSavedShowsUseCase
import com.chachadev.core.domain.usecase.SearchUseCase
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
    factoryOf(::GetShowDetailsUseCase)
    factoryOf(::GetShowEpisodesUseCase)
    factoryOf(::GetTrackDetailsUseCase)
    factoryOf(::GetCurrentUserPlaylistsUseCase)
    factoryOf(::GetUserSavedAlbumsUseCase)
    factoryOf(::GetUserSavedShowsUseCase)
    factoryOf(::GetUserSavedEpisodesUseCase)
    factoryOf(::GetRecentlyPlayedTracksUseCase)
}