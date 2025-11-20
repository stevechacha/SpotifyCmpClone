package com.chachadev.spotifycmpclone.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import com.chachadev.spotifycmpclone.presentation.ui.screen.HomeScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.LibraryScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.ProfileScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.SearchScreen


@Composable
fun ListContent(
    navController: NavHostController,
    onNavigateToDetail: (Screen) -> Unit,
    onCurrentScreenChange: (Screen) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.App.DashBoard.Home,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<Screen.App.DashBoard.Home> {
            HomeScreen(
                onAlbumClick = { albumId ->
                    onNavigateToDetail(Screen.App.Album(albumId))
                },
                onPlaylistClick = { playlistId ->
                    onNavigateToDetail(Screen.App.Playlist(playlistId))
                }
            )
        }
        composable<Screen.App.DashBoard.Search> {
            SearchScreen(
                onTrackClick = { trackId ->
                    onNavigateToDetail(Screen.App.Track(trackId))
                },
                onAlbumClick = { albumId ->
                    onNavigateToDetail(Screen.App.Album(albumId))
                },
                onArtistClick = { artistId ->
                    onNavigateToDetail(Screen.App.Artist(artistId))
                },
                onPlaylistClick = { playlistId ->
                    onNavigateToDetail(Screen.App.Playlist(playlistId))
                }
            )
        }
        composable<Screen.App.DashBoard.Library> {
            LibraryScreen()
        }
        composable<Screen.App.DashBoard.Profile> {
            ProfileScreen()
        }
    }

    // Update current screen based on navigation
    val currentDestination by navController.currentBackStackEntryAsState()
    LaunchedEffect(currentDestination) {
        currentDestination?.destination?.route?.let { route ->
            when {
                route.contains("Home") -> onCurrentScreenChange(Screen.App.DashBoard.Home)
                route.contains("Search") -> onCurrentScreenChange(Screen.App.DashBoard.Search)
                route.contains("Library") -> onCurrentScreenChange(Screen.App.DashBoard.Library)
                route.contains("Profile") -> onCurrentScreenChange(Screen.App.DashBoard.Profile)
            }
        }
    }
}
/*

@Composable
 fun ListContent(
    navController: NavHostController,
    onNavigateToDetail: (Screen) -> Unit,
    onCurrentScreenChange: (Screen) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.App.DashBoard.Home,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<Screen.App.DashBoard.Home> {
            HomeScreen(
                onAlbumClick = { albumId ->
                    onNavigateToDetail(Screen.App.Album(albumId))
                },
                onPlaylistClick = { playlistId ->
                    onNavigateToDetail(Screen.App.Playlist(playlistId))
                }
            )
        }
        composable<Screen.App.DashBoard.Search> {
            SearchScreen(
                onTrackClick = { trackId ->
                    onNavigateToDetail(Screen.App.Track(trackId))
                },
                onAlbumClick = { albumId ->
                    onNavigateToDetail(Screen.App.Album(albumId))
                },
                onArtistClick = { artistId ->
                    onNavigateToDetail(Screen.App.Artist(artistId))
                },
                onPlaylistClick = { playlistId ->
                    onNavigateToDetail(Screen.App.Playlist(playlistId))
                }
            )
        }
        composable<Screen.App.DashBoard.Library> {
            LibraryScreen()
        }
        composable<Screen.App.DashBoard.Profile> {
            ProfileScreen()
        }
    }

    // Update current screen based on navigation
    val currentDestination by navController.currentBackStackEntryAsState()
    LaunchedEffect(currentDestination) {
        currentDestination?.destination?.route?.let { route ->
            when {
                route.contains("Home") -> onCurrentScreenChange(Screen.App.DashBoard.Home)
                route.contains("Search") -> onCurrentScreenChange(Screen.App.DashBoard.Search)
                route.contains("Library") -> onCurrentScreenChange(Screen.App.DashBoard.Library)
                route.contains("Profile") -> onCurrentScreenChange(Screen.App.DashBoard.Profile)
            }
        }
    }
}*/
