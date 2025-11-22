package com.chachadev.spotifycmpclone.di

import com.chachadev.spotifycmpclone.presentation.viewmodel.AlbumDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.ArtistDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.HomeViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.LibraryViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.PlaylistDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.ProfileViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.SearchViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.TrackDetailViewModel
import org.koin.dsl.module


val screenViewModelModule = module {
    factory { HomeViewModel(get(), get(),get()) }
    factory { SearchViewModel(get()) }
    factory { AlbumDetailViewModel(get(), get()) }
    factory { PlaylistDetailViewModel(get(), get()) }
    factory { ArtistDetailViewModel(get(), get()) }
    factory { TrackDetailViewModel(get()) }
    factory { ProfileViewModel(get(), get()) }
    factory { LibraryViewModel(get(), get(), get(), get(), get()) }
}