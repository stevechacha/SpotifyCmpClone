package com.chachadev.spotifycmpclone.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.chachadev.spotifycmpclone.presentation.SpotifyCmpCloneAdaptiveLayout
import com.chachadev.spotifycmpclone.presentation.navigation.utils.NavigationGraph
import com.chachadev.spotifycmpclone.presentation.ui.screen.AlbumScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.ArtistScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.PlaylistScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.TrackScreen

fun NavGraphBuilder.appGraph(
    onNavigate: (Screen, Boolean) -> Unit,
    onNavigateBack: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {
    navigation<NavigationGraph.App>(startDestination = Screen.App.DashBoard) {
        composable<Screen.App.DashBoard> {
            SpotifyCmpCloneAdaptiveLayout(

            )
        }
        /*composable<Screen.App.DashBoard.Home> {
            HomeScreen(
                onAlbumClick = { onNavigate(Screen.App.Album(it),false) },
                onPlaylistClick = { onNavigate(Screen.App.Playlist(it), false) }
            )
        }
        composable<Screen.App.DashBoard.Search> {
            SearchScreen(
                onTrackClick = { onNavigate(Screen.App.Track(it),false) },
                onAlbumClick = { onNavigate(Screen.App.Album(it),false) },
                onArtistClick = { onNavigate(Screen.App.Artist(it),false) },
                onPlaylistClick = { onNavigate(Screen.App.Playlist(it),false) }
            )
        }
        composable<Screen.App.DashBoard.Library> {
            LibraryScreen()
        }
        composable<Screen.App.DashBoard.Profile> {
            ProfileScreen(

            )
        }*//*
        composable<Screen.App.Track> { backStackEntry ->
            val track = backStackEntry.toRoute<Screen.App.Track>()
            TrackScreen(
                trackId = track.trackId,
                onBack = onGoBack
            )
        }
        composable<Screen.App.Album> { backStackEntry ->
            val album = backStackEntry.toRoute<Screen.App.Album>()
            AlbumScreen(
                albumId = album.albumId,
                onTrackSelected = { onNavigate(Screen.App.Track(it),false) },
                onBack = onGoBack
            )
        }
        composable<Screen.App.Artist> { backStackEntry ->
            val artist = backStackEntry.toRoute<Screen.App.Artist>()
            ArtistScreen(
                artistId = artist.artistId,
                onTrackSelected = { onNavigate(Screen.App.Track(it),false) },
                onBack = onGoBack
            )
        }
        composable<Screen.App.Playlist> { backStackEntry ->
            val playlist = backStackEntry.toRoute<Screen.App.Playlist>()
            PlaylistScreen(
                playlistId = playlist.playlistId,
                onTrackSelected = { onNavigate(Screen.App.Track(it),false) },
                onBack = onGoBack
            )
        }*/
    }
}



