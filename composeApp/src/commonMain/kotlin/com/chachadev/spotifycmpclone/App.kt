package com.chachadev.spotifycmpclone

import androidx.compose.runtime.Composable
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
    // Read credentials dynamically (not cached) to ensure we get the latest values
    val credentials = PlatformSpotifyCredentials.get()
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