package com.chachadev.spotifycmpclone.presentation.adaptives

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chachadev.core.common.screen.Landscape
import com.chachadev.core.common.screen.Portrait
import com.chachadev.core.common.screen.ScreenOrientation
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import com.chachadev.spotifycmpclone.utils.DeviceConfiguration
import com.chachadev.spotifycmpclone.utils.createNoSpacingPaneScaffoldDirective
import com.chachadev.spotifycmpclone.utils.currentDeviceConfiguration
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SpotifyCmpCloneAdaptiveLayout(
    modifier: Modifier = Modifier,
    orientation: ScreenOrientation
) = Column(modifier = modifier){
    val listNavController = rememberNavController() // For the list pane's content (Home, Search, etc.)
    val detailNavController = rememberNavController() // For the detail pane's content (Album, Playlist, Artist)
    val trackNavController = rememberNavController() // For the track detail pane (third pane)
    val scaffoldDirective = createNoSpacingPaneScaffoldDirective()
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = scaffoldDirective
    )

    var currentListScreen by remember { mutableStateOf<Screen>(Screen.App.DashBoard.Home) }
    val detailBackStackEntry by detailNavController.currentBackStackEntryAsState()
    val trackBackStackEntry by trackNavController.currentBackStackEntryAsState()
    
    val isDetailFlowActive = remember(detailBackStackEntry) {
        detailBackStackEntry?.destination?.route?.let { route ->
            !route.contains("EmptyDetailScreenDestination", ignoreCase = true) &&
            route.isNotEmpty()
        } ?: false
    }
    
    val isTrackFlowActive = remember(trackBackStackEntry) {
        trackBackStackEntry?.destination?.route?.let { route ->
            !route.contains("EmptyDetailScreenDestination", ignoreCase = true) &&
            route.isNotEmpty()
        } ?: false
    }
    
    val scope = rememberCoroutineScope()

    val deviceConfiguration = currentDeviceConfiguration()
    
    // Shared search query state for landscape top bar and SearchScreen
    var sharedSearchQuery by remember { mutableStateOf("") }
    /*val isWideScreen = deviceConfiguration.isWideScreen
    val isMobile = deviceConfiguration.isMobile*/

    // Initialize to show list pane on start
    LaunchedEffect(Unit) {
        if (scaffoldNavigator.currentDestination == null) {
            scaffoldNavigator.navigateTo(ThreePaneScaffoldRole.Secondary)
        }
    }

    // Track detail pane state to ensure proper navigation
    LaunchedEffect(isDetailFlowActive, isTrackFlowActive, orientation is Landscape, deviceConfiguration) {
        val currentPaneRole = scaffoldNavigator.currentDestination?.pane
        val targetPaneRole = if (orientation is Landscape && deviceConfiguration == DeviceConfiguration.DESKTOP) {
            when {
                isTrackFlowActive -> ThreePaneScaffoldRole.Tertiary // Track (all 3 panes visible)
                isDetailFlowActive -> ThreePaneScaffoldRole.Primary // Detail (list + detail visible)
                else -> ThreePaneScaffoldRole.Secondary // List (only list visible)
            }
        } else {
            when {
                isTrackFlowActive -> ThreePaneScaffoldRole.Tertiary // Track
                isDetailFlowActive -> ThreePaneScaffoldRole.Primary // Detail (hides list)
                else -> ThreePaneScaffoldRole.Secondary // List (hides detail)
            }
        }
        scope.launch {
            scaffoldNavigator.navigateTo(targetPaneRole)
        }
    }

    AdaptiveBackHandler(
        orientation = orientation,
        scaffoldNavigator = scaffoldNavigator,
        isDetailFlowActive = isDetailFlowActive,
        isTrackFlowActive = isTrackFlowActive,
        detailNavController = detailNavController,
        trackNavController = trackNavController
    )

    val shouldShowNavigationBar = !isDetailFlowActive && !isTrackFlowActive

    LandscapeTopNavigationBar(
        orientation = orientation,
        searchQuery = sharedSearchQuery,
        onSearchQueryChange = { sharedSearchQuery = it },
        listNavController = listNavController,
        detailNavController = detailNavController,
        currentListScreen = currentListScreen,
        onCurrentListScreenChange = { currentListScreen = it },
        isDetailFlowActive = isDetailFlowActive
    )
    NavigationSuiteScaffold(
        navigationItems = {
            PortraitNavigationBar(
                orientation = orientation,
                shouldShow = shouldShowNavigationBar,
                currentListScreen = currentListScreen,
                onCurrentListScreenChange = { currentListScreen = it },
                listNavController = listNavController
            )
        },
    ) {
        ListDetailPaneScaffold(
            directive = scaffoldDirective,
            value = scaffoldNavigator.scaffoldValue,
            listPane = {
                if (orientation is Portrait)
                ListContent(
                    navController = listNavController,
                    onNavigateToDetail = { destination ->
                        navigateToDetail(destination, detailNavController)
                    },
                    onCurrentScreenChange = { screen ->
                        currentListScreen = screen
                    },
                    initialSearchQuery = sharedSearchQuery,
                    orientation = orientation,
                    onClearSearch = {
                        sharedSearchQuery = ""
                    }
                )
                else DesktopLibrary()
            },
            detailPane = {
                DetailPaneNavHost(
                    navController = detailNavController,
                    trackNavController = trackNavController,
                    listNavController = listNavController,
                    onTrackSelected = { trackId ->
                        // Navigate to track detail in the extra pane
                        trackNavController.navigate(Screen.App.Track(trackId)) {
                            launchSingleTop = true
                        }
                        // On desktop, ensure the scaffold navigates to Tertiary pane to show the track
                        if (orientation is Portrait && deviceConfiguration == DeviceConfiguration.DESKTOP) {
                            scope.launch {
                                scaffoldNavigator.navigateTo(ThreePaneScaffoldRole.Tertiary)
                            }
                        }
                    },
                    onNavigateToDetail = { destination ->
                        navigateToDetail(destination, detailNavController)
                    },
                    onCurrentScreenChange = { screen ->
                        currentListScreen = screen
                    },
                    orientation = orientation,
                    searchQuery = sharedSearchQuery,
                    onClearSearch = {
                        sharedSearchQuery = ""
                    }
                )
            },
            extraPane = {
                // Third pane for track details
                TrackDetailPaneNavHost(
                    orientation = orientation,
                    navController = trackNavController
                )
            }
        )
    }
}