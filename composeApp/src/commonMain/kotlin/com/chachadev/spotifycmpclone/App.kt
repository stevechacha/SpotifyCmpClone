package com.chachadev.spotifycmpclone

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.chachadev.spotifycmpclone.di.PlatformSpotifyCredentials
import com.chachadev.spotifycmpclone.di.SpotifyCredentials
import com.chachadev.spotifycmpclone.di.appModule
import com.chachadev.spotifycmpclone.presentation.navigation.AppNavigation
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import com.chachadev.spotifycmpclone.presentation.ui.theme.SpotifyTheme
import com.chachadev.spotifycmpclone.presentation.viewmodel.AlbumDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.ArtistDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.HomeViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.PlaylistDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.SearchViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
fun App() {
    // Initialize Koin only once
    // In production, you should get the access token from OAuth flow
    // For now, using empty string - you'll need to implement Spotify OAuth
    val credentials = remember { PlatformSpotifyCredentials.get() }

    KoinApplication({
        modules(appModule())
        properties(
            mapOf(
                "spotify_client_id" to credentials.clientId,
                "spotify_client_secret" to credentials.clientSecret,
                "spotify_redirect_uri" to credentials.redirectUri
            )
        )
    }) {

        SpotifyTheme {
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
            val navigationItems = remember {
                listOf(
                    NavigationItem("Home", Icons.Default.Home, Screen.Home),
                    NavigationItem("Search", Icons.Default.Search, Screen.Search),
                    NavigationItem("Library", Icons.Default.LibraryMusic, Screen.Library),
                    NavigationItem("Profile", Icons.Default.Person, Screen.Profile)
                )
            }

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        navigationItems.forEach { item ->
                            val isSelected = when (item.screen) {
                                Screen.Home -> currentScreen is Screen.Home
                                Screen.Search -> currentScreen is Screen.Search ||
                                    currentScreen is Screen.Track ||
                                    currentScreen is Screen.Album ||
                                    currentScreen is Screen.Artist ||
                                    currentScreen is Screen.Playlist
                                Screen.Library -> currentScreen is Screen.Library
                                Screen.Profile -> currentScreen is Screen.Profile
                                is Screen.Album -> currentScreen is Screen.Album
                                is Screen.Artist -> currentScreen is Screen.Artist
                                is Screen.Playlist -> currentScreen is Screen.Playlist
                                is Screen.Track -> currentScreen is Screen.Track

                            }

                            NavigationBarItem(
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                label = { Text(item.label) },
                                selected = isSelected,
                                onClick = { currentScreen = item.screen }
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    // Inject ViewModels from Koin
                    val homeViewModel: HomeViewModel = koinInject()
                    val searchViewModel: SearchViewModel = koinInject()
                    val albumDetailViewModel: AlbumDetailViewModel = koinInject()
                    val playlistDetailViewModel: PlaylistDetailViewModel = koinInject()
                    val artistDetailViewModel: ArtistDetailViewModel = koinInject()

                    AppNavigation(
                        homeViewModel = homeViewModel,
                        searchViewModel = searchViewModel,
                        albumDetailViewModel = albumDetailViewModel,
                        playlistDetailViewModel = playlistDetailViewModel,
                        artistDetailViewModel = artistDetailViewModel,
                        currentScreen = currentScreen,
                        onNavigate = { currentScreen = it }
                    )
                }
            }
        }
    }
}

private data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)