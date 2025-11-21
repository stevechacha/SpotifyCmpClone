package com.chachadev.spotifycmpclone.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import com.chachadev.spotifycmpclone.presentation.ui.screen.AlbumScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.ArtistScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.HomeScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.PlaylistScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.SearchScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.TrackScreen
import com.chachadev.core.common.screen.Landscape
import com.chachadev.core.common.screen.ScreenOrientation


@Composable
fun DetailPaneNavHost(
    navController: NavHostController,
    trackNavController: NavHostController,
    listNavController: NavHostController,
    onTrackSelected: (String) -> Unit,
    onNavigateToDetail: (Screen) -> Unit,
    onCurrentScreenChange: (Screen) -> Unit,
    orientation: ScreenOrientation,
    searchQuery: String = ""
) {
    NavHost(
        navController = navController,
        startDestination = Screen.App.EmptyDetailScreenDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<Screen.App.EmptyDetailScreenDestination> {
            // In landscape mode, show SearchScreen when there's a search query, otherwise show HomeScreen
            if (orientation is Landscape && searchQuery.isNotEmpty()) {
                SearchScreen(
                    initialQuery = searchQuery,
                    onTrackClick = { trackId ->
                        onTrackSelected(trackId)
                    },
                    onAlbumClick = { albumId ->
                        onNavigateToDetail(Screen.App.Album(albumId))
                    },
                    onArtistClick = { artistId ->
                        onNavigateToDetail(Screen.App.Artist(artistId))
                    },
                    onPlaylistClick = { playlistId ->
                        onNavigateToDetail(Screen.App.Playlist(playlistId))
                    },
                    orientation = orientation
                )
            } else {
                // Should Return Either PlaylistDetail,ArtistDetails,AlbumDetails when navigated back
                HomeScreen(
                    onAlbumClick = { albumId ->
                        onNavigateToDetail(Screen.App.Album(albumId))
                    },
                    onPlaylistClick = { playlistId ->
                        onNavigateToDetail(Screen.App.Playlist(playlistId))
                    }
                )
            }
        }
        composable<Screen.App.Album> { backStackEntry ->
            val album = backStackEntry.toRoute<Screen.App.Album>()
            AlbumScreen(
                albumId = album.albumId,
                onTrackSelected = onTrackSelected, // Pass to track detail pane
                onBack = {
                    // Navigate detail pane to empty screen
                    // Also navigate list pane back to Home if needed
                    navController.navigate(Screen.App.EmptyDetailScreenDestination) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Screen.App.Artist> { backStackEntry ->
            val artist = backStackEntry.toRoute<Screen.App.Artist>()
            ArtistScreen(
                artistId = artist.artistId,
                onTrackSelected = onTrackSelected, // Pass to track detail pane
                onBack = {
                    // Navigate detail pane to empty screen
                    // Also navigate list pane back to Home
                    navController.navigate(Screen.App.EmptyDetailScreenDestination) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Screen.App.Playlist> { backStackEntry ->
            val playlist = backStackEntry.toRoute<Screen.App.Playlist>()
            PlaylistScreen(
                playlistId = playlist.playlistId,
                onTrackSelected = onTrackSelected, // Pass to track detail pane
                onBack = {
                    // Navigate detail pane to empty screen
                    // Also navigate list pane back to Home
                    navController.navigate(Screen.App.EmptyDetailScreenDestination) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        // Track navigation from list pane goes here but opens in extra pane
        composable<Screen.App.Track> { backStackEntry ->
            val track = backStackEntry.toRoute<Screen.App.Track>()
            // If track is selected from list, navigate to track pane
            LaunchedEffect(track.trackId) {
                trackNavController.navigate(Screen.App.Track(track.trackId)) {
                    launchSingleTop = true
                }
                // Go back to empty detail
                navController.navigate(Screen.App.EmptyDetailScreenDestination) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    launchSingleTop = true
                }
            }
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}
/*

@Composable
fun DetailPaneNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.App.EmptyDetailScreenDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<Screen.App.EmptyDetailScreenDestination> {
            // Empty detail screen - shows nothing when no detail is selected
            Box(
                modifier = Modifier.fillMaxSize()
            )
        }
        composable<Screen.App.Track> { backStackEntry ->
            val track = backStackEntry.toRoute<Screen.App.Track>()
            TrackScreen(
                trackId = track.trackId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.App.Album> { backStackEntry ->
            val album = backStackEntry.toRoute<Screen.App.Album>()
            AlbumScreen(
                albumId = album.albumId,
                onTrackSelected = { trackId ->
                    navController.navigate(Screen.App.Track(trackId))
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.App.Artist> { backStackEntry ->
            val artist = backStackEntry.toRoute<Screen.App.Artist>()
            ArtistScreen(
                artistId = artist.artistId,
                onTrackSelected = { trackId ->
                    navController.navigate(Screen.App.Track(trackId))
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.App.Playlist> { backStackEntry ->
            val playlist = backStackEntry.toRoute<Screen.App.Playlist>()
            PlaylistScreen(
                playlistId = playlist.playlistId,
                onTrackSelected = { trackId ->
                    navController.navigate(Screen.App.Track(trackId))
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}*/
