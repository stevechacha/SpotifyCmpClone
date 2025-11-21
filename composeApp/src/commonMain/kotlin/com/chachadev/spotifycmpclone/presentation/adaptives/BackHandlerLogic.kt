package com.chachadev.spotifycmpclone.presentation.adaptives

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import com.chachadev.core.common.screen.Portrait
import com.chachadev.core.common.screen.ScreenOrientation
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
fun <T> AdaptiveBackHandler(
    orientation: ScreenOrientation,
    scaffoldNavigator: ThreePaneScaffoldNavigator<T>,
    isDetailFlowActive: Boolean,
    isTrackFlowActive: Boolean,
    detailNavController: NavHostController,
    trackNavController: NavHostController
){
    val scope = rememberCoroutineScope()
    
    BackHandler(
        enabled = (orientation is Portrait || scaffoldNavigator.canNavigateBack()) || isDetailFlowActive || isTrackFlowActive
    ) {
        when {
            isTrackFlowActive -> {
                // Handle track pane back navigation
                if (trackNavController.previousBackStackEntry == null) {
                    trackNavController.navigate(Screen.App.EmptyDetailScreenDestination) {
                        popUpTo(trackNavController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                    // On mobile, navigate back to detail pane
                    if (orientation is Portrait && scaffoldNavigator.currentDestination?.pane == ThreePaneScaffoldRole.Tertiary) {
                        scope.launch { scaffoldNavigator.navigateTo(ThreePaneScaffoldRole.Primary) }
                    }
                } else {
                    trackNavController.popBackStack()
                }
            }
            isDetailFlowActive -> {
                if (detailNavController.previousBackStackEntry == null) {
                    detailNavController.navigate(Screen.App.EmptyDetailScreenDestination) {
                        popUpTo(detailNavController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                    if (orientation is Portrait && scaffoldNavigator.currentDestination?.pane == ThreePaneScaffoldRole.Primary) {
                        scope.launch { scaffoldNavigator.navigateTo(ThreePaneScaffoldRole.Secondary) }
                    }
                } else {
                    detailNavController.popBackStack()
                }
            }
            orientation is Portrait && scaffoldNavigator.canNavigateBack() -> {
                scope.launch { scaffoldNavigator.navigateBack() }
            }
        }
    }
}

