package com.chachadev.spotifycmpclone.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import com.chachadev.spotifycmpclone.presentation.ui.screen.TrackScreen

@Composable
fun TrackDetailPaneNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.App.EmptyDetailScreenDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        // Should Return Either PlaylistDetail,ArtistDetails,AlbumDetails when navigated back

        composable<Screen.App.EmptyDetailScreenDestination> { backStackEntry ->
            val track = backStackEntry.toRoute<Screen.App.EmptyDetailScreenDestination>()

            // Empty track detail screen
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                Text("Empty Screee", textAlign = TextAlign.Center, fontSize = 56.sp)
            }
        }
        composable<Screen.App.Track> { backStackEntry ->
            val track = backStackEntry.toRoute<Screen.App.Track>()
            TrackScreen(
                trackId = track.trackId,
                onBack = {
                    navController.navigate(Screen.App.EmptyDetailScreenDestination) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}