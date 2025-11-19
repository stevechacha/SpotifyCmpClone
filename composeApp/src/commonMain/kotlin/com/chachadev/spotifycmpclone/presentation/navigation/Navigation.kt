package com.chachadev.spotifycmpclone.presentation.navigation

import androidx.compose.runtime.Composable
import com.chachadev.spotifycmpclone.presentation.ui.screen.AlbumScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.ArtistScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.HomeScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.LibraryScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.PlaylistScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.ProfileScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.SearchScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.TrackScreen
import com.chachadev.spotifycmpclone.presentation.viewmodel.AlbumDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.ArtistDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.HomeViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.PlaylistDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.ProfileViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.SearchViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.TrackDetailViewModel
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable data object Home : Screen()
    @Serializable data object Search : Screen()
    @Serializable data object Library : Screen()
    @Serializable data object Profile : Screen()
    @Serializable data class Track(val trackId: String) : Screen()
    @Serializable data class Album(val albumId: String) : Screen()
    @Serializable data class Artist(val artistId: String) : Screen()
    @Serializable data class Playlist(val playlistId: String) : Screen()
}

@Composable
fun AppNavigation(
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    albumDetailViewModel: AlbumDetailViewModel,
    playlistDetailViewModel: PlaylistDetailViewModel,
    artistDetailViewModel: ArtistDetailViewModel,
    trackDetailViewModel: TrackDetailViewModel,
    profileViewModel: ProfileViewModel,
    currentScreen: Screen = Screen.Home,
    onNavigate: (Screen) -> Unit = {}
) {
    when (currentScreen) {
        is Screen.Home -> {
            HomeScreen(
                viewModel = homeViewModel,
                onAlbumClick = { onNavigate(Screen.Album(it)) },
                onPlaylistClick = { onNavigate(Screen.Playlist(it)) }
            )
        }
        is Screen.Search -> {
            SearchScreen(
                viewModel = searchViewModel,
                onTrackClick = { onNavigate(Screen.Track(it)) },
                onAlbumClick = { onNavigate(Screen.Album(it)) },
                onArtistClick = { onNavigate(Screen.Artist(it)) },
                onPlaylistClick = { onNavigate(Screen.Playlist(it)) }
            )
        }
        is Screen.Library -> {
            LibraryScreen()
        }
        is Screen.Profile -> {
            ProfileScreen(viewModel = profileViewModel)
        }
        is Screen.Track -> {
            TrackScreen(
                trackId = currentScreen.trackId,
                viewModel = trackDetailViewModel,
                onBack = { onNavigate(Screen.Search) }
            )
        }
        is Screen.Album -> {
            AlbumScreen(
                albumId = currentScreen.albumId,
                viewModel = albumDetailViewModel,
                onTrackSelected = { onNavigate(Screen.Track(it)) },
                onBack = { onNavigate(Screen.Search) }
            )
        }
        is Screen.Artist -> {
            ArtistScreen(
                artistId = currentScreen.artistId,
                viewModel = artistDetailViewModel,
                onTrackSelected = { onNavigate(Screen.Track(it)) },
                onBack = { onNavigate(Screen.Search) }
            )
        }
        is Screen.Playlist -> {
            PlaylistScreen(
                playlistId = currentScreen.playlistId,
                viewModel = playlistDetailViewModel,
                onTrackSelected = { onNavigate(Screen.Track(it)) },
                onBack = { onNavigate(Screen.Search) }
            )
        }
    }
}

