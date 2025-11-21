package com.chachadev.spotifycmpclone.presentation.adaptives

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.chachadev.core.common.screen.Portrait
import com.chachadev.core.common.screen.ScreenOrientation
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
fun <T> AdaptiveBackHandler(
    orientation: ScreenOrientation,
    scaffoldNavigator: ThreePaneScaffoldNavigator<T>,
    isDetailFlowActive: Boolean,
    isTrackFlowActive: Boolean,
    detailNavController: NavHostController,
    trackNavController: NavHostController,
    listNavController: NavHostController,
    currentListScreen: Screen,
    onCurrentListScreenChange: (Screen) -> Unit
) {
    val scope = rememberCoroutineScope()

    BackHandler(
        enabled = (orientation is Portrait || scaffoldNavigator.canNavigateBack()) || isDetailFlowActive || isTrackFlowActive
    ) {
        handleBackNavigation(
            orientation = orientation,
            scaffoldNavigator = scaffoldNavigator,
            isDetailFlowActive = isDetailFlowActive,
            isTrackFlowActive = isTrackFlowActive,
            detailNavController = detailNavController,
            trackNavController = trackNavController,
            listNavController = listNavController,
            currentListScreen = currentListScreen,
            onCurrentListScreenChange = onCurrentListScreenChange,
            scope = scope
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun <T> handleBackNavigation(
    orientation: ScreenOrientation,
    scaffoldNavigator: ThreePaneScaffoldNavigator<T>,
    isDetailFlowActive: Boolean,
    isTrackFlowActive: Boolean,
    detailNavController: NavHostController,
    trackNavController: NavHostController,
    listNavController: NavHostController,
    currentListScreen: Screen,
    onCurrentListScreenChange: (Screen) -> Unit,
    scope: CoroutineScope
) {
    when {
        isTrackFlowActive -> {
            if (trackNavController.previousBackStackEntry == null) {
                trackNavController.navigate(Screen.App.EmptyDetailScreenDestination) {
                    popUpTo(trackNavController.graph.findStartDestination().id) { inclusive = true }
                    launchSingleTop = true
                }
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
        currentListScreen != Screen.App.DashBoard.Home -> {
            onCurrentListScreenChange(Screen.App.DashBoard.Home)
            listNavController.navigate(Screen.App.DashBoard.Home) {
                popUpTo(listNavController.graph.findStartDestination().id) { inclusive = false }
                launchSingleTop = true
            }
        }
        orientation is Portrait && scaffoldNavigator.canNavigateBack() -> {
            scope.launch { scaffoldNavigator.navigateBack() }
        }
    }
}
