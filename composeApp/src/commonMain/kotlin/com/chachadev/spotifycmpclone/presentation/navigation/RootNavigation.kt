package com.chachadev.spotifycmpclone.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.chachadev.core.common.screen.ScreenOrientation
import com.chachadev.spotifycmpclone.presentation.navigation.utils.NavigationGraph
import com.chachadev.spotifycmpclone.presentation.navigation.utils.onGoBack
import com.chachadev.spotifycmpclone.presentation.navigation.utils.onNavigate
import com.chachadev.spotifycmpclone.presentation.navigation.utils.onNavigateBack
import com.chachadev.spotifycmpclone.presentation.navigation.utils.onNavigateGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    orientation: ScreenOrientation
) {
    NavHost(
        navController = navController,
        startDestination =  NavigationGraph.App
    ) {
        authGraph(
            onNavigate = navController::onNavigate,
            onNavigateGraph = navController::onNavigateGraph,
        )
        appGraph(
            onNavigate = navController::onNavigate,
            onNavigateBack = navController::onNavigateBack,
            onGoBack = navController::onGoBack,
            orientation = orientation
        )
    }
}