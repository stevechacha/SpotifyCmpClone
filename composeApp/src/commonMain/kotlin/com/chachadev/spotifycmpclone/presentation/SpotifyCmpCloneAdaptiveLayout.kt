package com.chachadev.spotifycmpclone.presentation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import com.chachadev.spotifycmpclone.utils.createNoSpacingPaneScaffoldDirective
import com.chachadev.spotifycmpclone.utils.currentDeviceConfiguration
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SpotifyCmpCloneAdaptiveLayout(){
//    val listDetailNavigator = rememberSupportingPaneScaffoldNavigator<ThreePaneScaffoldRole>()
    val listNavController = rememberNavController() // For the list pane's content (Home, Search, etc.)
    val detailNavController = rememberNavController() // For the detail pane's content (Track, Album, etc.)
    val scaffoldDirective = createNoSpacingPaneScaffoldDirective()
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = scaffoldDirective
    )

    // Track current list screen
    var currentListScreen by remember { mutableStateOf<Screen>(Screen.App.DashBoard.Home) }

    val detailBackStackEntry by detailNavController.currentBackStackEntryAsState()
    val isDetailFlowActive = remember(detailBackStackEntry) {
        // Check if we have a detail screen active (not the empty detail screen)
        detailBackStackEntry?.destination?.route?.let { route ->
            !route.contains("EmptyDetailScreenDestination", ignoreCase = true)
        } ?: false
    }
    val scope = rememberCoroutineScope()

    val deviceConfiguration = currentDeviceConfiguration()
    val isWideScreen = deviceConfiguration.isWideScreen
    val isMobile = deviceConfiguration.isMobile

    LaunchedEffect(isDetailFlowActive, scaffoldNavigator.currentDestination, isWideScreen) {
        val currentPaneRole = scaffoldNavigator.currentDestination?.pane

        // On wide screens (desktop/tablet landscape), show both panes when detail is active
        // On mobile, navigate between panes (only one visible at a time)
        val targetPaneRole = if (isWideScreen) {
            // On wide screens, if detail is active, focus on detail pane but keep both visible
            // If no detail, focus on list pane
            if (isDetailFlowActive) {
                ThreePaneScaffoldRole.Primary // Detail (but both panes visible)
            } else {
                ThreePaneScaffoldRole.Secondary // List
            }
        } else {
            // On mobile, navigate between panes (only one visible)
            if (isDetailFlowActive) {
                ThreePaneScaffoldRole.Primary // Detail (hides list)
            } else {
                ThreePaneScaffoldRole.Secondary // List (hides detail)
            }
        }

        if (currentPaneRole != targetPaneRole) {
            scaffoldNavigator.navigateTo(targetPaneRole)
        }
    }

    BackHandler(enabled = (isMobile && scaffoldNavigator.canNavigateBack()) || isDetailFlowActive) {
        if (isDetailFlowActive) {
            if (detailNavController.previousBackStackEntry == null) {
                // On wide screens, just clear detail pane but keep list visible
                // On mobile, navigate back to list pane
                detailNavController.navigate(Screen.App.EmptyDetailScreenDestination) {
                    popUpTo(detailNavController.graph.findStartDestination().id) { inclusive = true }
                    launchSingleTop = true
                }
                // On mobile, also navigate back to list pane
                if (isMobile && scaffoldNavigator.currentDestination?.pane == ThreePaneScaffoldRole.Primary) {
                    scope.launch { scaffoldNavigator.navigateTo(ThreePaneScaffoldRole.Secondary) }
                }
            } else {
                detailNavController.popBackStack()
            }
        } else if (isMobile && scaffoldNavigator.canNavigateBack()) {
            // On mobile, handle scaffold-level back navigation
            scope.launch { scaffoldNavigator.navigateBack() }
        }
        // On wide screens, if no detail is active, system handles back (might close app)
    }

    /*NavigationSuiteScaffold(
        navigationSuiteItems = {
            navigationSuiteItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = currentListScreen is Screen.App.DashBoard.Home,
                onClick = {
                    currentListScreen = Screen.App.DashBoard.Home
                    listNavController.navigate(Screen.App.DashBoard.Home) {
                        popUpTo(listNavController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
            navigationSuiteItem(
                icon = Icons.Default.Search,
                label = "Search",
                selected = currentListScreen is Screen.App.DashBoard.Search,
                onClick = {
                    currentListScreen = Screen.App.DashBoard.Search
                    listNavController.navigate(Screen.App.DashBoard.Search) {
                        popUpTo(listNavController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
            navigationSuiteItem(
                icon = Icons.Default.LibraryMusic,
                label = "Library",
                selected = currentListScreen is Screen.App.DashBoard.Library,
                onClick = {
                    currentListScreen = Screen.App.DashBoard.Library
                    listNavController.navigate(Screen.App.DashBoard.Library) {
                        popUpTo(listNavController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
            navigationSuiteItem(
                icon = Icons.Default.Person,
                label = "Profile",
                selected = currentListScreen is Screen.App.DashBoard.Profile,
                onClick = {
                    currentListScreen = Screen.App.DashBoard.Profile
                    listNavController.navigate(Screen.App.DashBoard.Profile) {
                        popUpTo(listNavController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        },
        paneScaffoldDirective = scaffoldDirective
    ) {*/

    // NavigationSuiteScaffold with empty items - can be expanded later
    NavigationSuiteScaffold(
        navigationItems = {
            // Navigation items can be added here when needed
        },
    ) {
        ListDetailPaneScaffold(
            directive = scaffoldDirective,
            value = scaffoldNavigator.scaffoldValue,
            listPane = {
                ListContent(
                    navController = listNavController,
                    onNavigateToDetail = { destination ->
                        detailNavController.navigate(destination) {
                            launchSingleTop = true
                        }
                    },
                    onCurrentScreenChange = { screen ->
                        currentListScreen = screen
                    }
                )
            },
            detailPane = {
                DetailPaneNavHost(
                    navController = detailNavController
                )
            }
        )
    }
}