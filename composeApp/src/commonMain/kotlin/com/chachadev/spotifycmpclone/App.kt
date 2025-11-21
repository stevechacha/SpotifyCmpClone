package com.chachadev.spotifycmpclone

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.rememberNavController
import com.chachadev.core.common.screen.rememberScreenOrientation
import com.chachadev.spotifycmpclone.di.PlatformSpotifyCredentials
import com.chachadev.spotifycmpclone.di.appModule
import com.chachadev.spotifycmpclone.presentation.navigation.NavigationRoot
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import com.chachadev.spotifycmpclone.presentation.ui.theme.SpotifyTheme
import com.chachadev.spotifycmpclone.presentation.viewmodel.AlbumDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.ArtistDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.HomeViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.PlaylistDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.ProfileViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.SearchViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.TrackDetailViewModel
import org.koin.compose.KoinApplication

@Composable
fun App() {
    // Initialize Koin only once
    // In production, you should get the access token from OAuth flow
    // For now, using empty string - you'll need to implement Spotify OAuth
    val credentials = remember { PlatformSpotifyCredentials.get() }
    val orientation = rememberScreenOrientation()


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
            val navController = rememberNavController()
            NavigationRoot(
                navController = navController,
                orientation = orientation
            )

        }

    }
}

private data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)

@Composable
fun AppNavigation(
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    albumDetailViewModel: AlbumDetailViewModel,
    playlistDetailViewModel: PlaylistDetailViewModel,
    artistDetailViewModel: ArtistDetailViewModel,
    trackDetailViewModel: TrackDetailViewModel,
    profileViewModel: ProfileViewModel,
    currentScreen: Screen.App,
    onNavigate: () -> Unit
) {
    when (currentScreen) {
        Screen.App.DashBoard -> {}
        Screen.App.EmptyDetailScreenDestination -> {}
        Screen.App -> {}
    }

}