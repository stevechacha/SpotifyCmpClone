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
import com.chachadev.spotifycmpclone.data.auth.AuthManager
import com.chachadev.spotifycmpclone.presentation.navigation.AppNavigation
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import com.chachadev.spotifycmpclone.presentation.ui.screen.AuthScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.WelcomeScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.getStoredAuthCode
import com.chachadev.spotifycmpclone.presentation.ui.theme.SpotifyTheme
import com.chachadev.spotifycmpclone.presentation.viewmodel.AlbumDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.ArtistDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.HomeViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.PlaylistDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.ProfileViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.SearchViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.TrackDetailViewModel
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
            // Inject AuthManager to check authentication state
            val authManager: AuthManager = koinInject()
            val isSignedIn by authManager.isSignedIn.collectAsState()

            var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
            val navigationItems = remember {
                listOf(
                    NavigationItem("Home", Icons.Default.Home, Screen.Home),
                    NavigationItem("Search", Icons.Default.Search, Screen.Search),
                    NavigationItem("Library", Icons.Default.LibraryMusic, Screen.Library),
                    NavigationItem("Profile", Icons.Default.Person, Screen.Profile)
                )
            }

           /* if (!isSignedIn) {
                // Show Welcome Screen if not signed in
                var showAuthScreen by remember { mutableStateOf(false) }
                val profileViewModel: ProfileViewModel = koinInject()
                
                profileViewModel.onSignInSuccess = {
                    // On successful sign in, the state will update and show main app
                }
                
                // Check for OAuth callback (for web platform)
                LaunchedEffect(Unit) {
                    val storedCode = getStoredAuthCode()
                    if (storedCode != null) {
                        // We have an auth code from callback, show auth screen to process it
                        showAuthScreen = true
                    }
                }
                
                if (showAuthScreen) {
                    AuthScreen(
                        viewModel = profileViewModel,
                        onAuthSuccess = {
                            // Auth success is handled by ProfileViewModel
                        },
                        onAuthFailure = { error ->
                            // Handle auth failure
                            showAuthScreen = false
                        }
                    )
                } else {
                    WelcomeScreen(
                        onSignInClick = { showAuthScreen = true }
                    )
                }
            } else {*/
                // Show main app if signed in
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
                                    else -> false
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
                        val trackDetailViewModel: TrackDetailViewModel = koinInject()
                        val profileViewModel: ProfileViewModel = koinInject()

                        AppNavigation(
                            homeViewModel = homeViewModel,
                            searchViewModel = searchViewModel,
                            albumDetailViewModel = albumDetailViewModel,
                            playlistDetailViewModel = playlistDetailViewModel,
                            artistDetailViewModel = artistDetailViewModel,
                            trackDetailViewModel = trackDetailViewModel,
                            profileViewModel = profileViewModel,
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