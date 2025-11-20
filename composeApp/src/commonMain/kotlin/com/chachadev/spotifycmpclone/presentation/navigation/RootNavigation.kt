package com.chachadev.spotifycmpclone.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.chachadev.spotifycmpclone.presentation.navigation.utils.NavigationGraph
import com.chachadev.spotifycmpclone.presentation.navigation.utils.onGoBack
import com.chachadev.spotifycmpclone.presentation.navigation.utils.onNavigate
import com.chachadev.spotifycmpclone.presentation.navigation.utils.onNavigateBack
import com.chachadev.spotifycmpclone.presentation.navigation.utils.onNavigateGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: NavigationGraph = NavigationGraph.App
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(
            onNavigate = navController::onNavigate,
            onNavigateGraph = navController::onNavigateGraph,
        )
        appGraph(
            onNavigate = navController::onNavigate,
            onNavigateBack = navController::onNavigateBack,
            onGoBack = navController::onGoBack,
        )
    }
}