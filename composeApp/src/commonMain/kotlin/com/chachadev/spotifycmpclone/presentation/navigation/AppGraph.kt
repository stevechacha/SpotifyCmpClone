package com.chachadev.spotifycmpclone.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.chachadev.core.common.screen.ScreenOrientation
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
    orientation: ScreenOrientation
) {
    navigation<NavigationGraph.App>(startDestination = Screen.App.DashBoard) {
        composable<Screen.App.DashBoard> {
            SpotifyCmpCloneAdaptiveLayout(
                orientation = orientation,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}



