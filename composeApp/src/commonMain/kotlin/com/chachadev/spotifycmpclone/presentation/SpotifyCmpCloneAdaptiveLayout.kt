package com.chachadev.spotifycmpclone.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import com.chachadev.spotifycmpclone.presentation.ui.screen.AlbumScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.ArtistScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.HomeScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.LibraryScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.PlaylistScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.ProfileScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.SearchScreen
import com.chachadev.spotifycmpclone.presentation.ui.screen.TrackScreen
import com.chachadev.spotifycmpclone.utils.DeviceConfiguration
import com.chachadev.spotifycmpclone.utils.createNoSpacingPaneScaffoldDirective
import com.chachadev.spotifycmpclone.utils.currentDeviceConfiguration
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SpotifyCmpCloneAdaptiveLayout(){
    val listNavController = rememberNavController() // For the list pane's content (Home, Search, etc.)
    val detailNavController = rememberNavController() // For the detail pane's content (Album, Playlist, Artist)
    val trackNavController = rememberNavController() // For the track detail pane (third pane)
    val scaffoldDirective = createNoSpacingPaneScaffoldDirective()
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = scaffoldDirective
    )

    // Track current list screen
    var currentListScreen by remember { mutableStateOf<Screen>(Screen.App.DashBoard.Home) }

    val detailBackStackEntry by detailNavController.currentBackStackEntryAsState()
    val trackBackStackEntry by trackNavController.currentBackStackEntryAsState()
    
    val isDetailFlowActive = remember(detailBackStackEntry) {
        // Check if we have a detail screen active (not the empty detail screen)
        // Check route string to see if we're at EmptyDetailScreenDestination
        // Since graph might not be initialized yet, we check the route directly
        detailBackStackEntry?.destination?.route?.let { route ->
            !route.contains("EmptyDetailScreenDestination", ignoreCase = true) &&
            route.isNotEmpty()
        } ?: false
    }
    
    val isTrackFlowActive = remember(trackBackStackEntry) {
        // Check if we have a track screen active
        // Check route string to see if we're at EmptyDetailScreenDestination
        // Since graph might not be initialized yet, we check the route directly
        trackBackStackEntry?.destination?.route?.let { route ->
            !route.contains("EmptyDetailScreenDestination", ignoreCase = true) &&
            route.isNotEmpty()
        } ?: false
    }
    
    val scope = rememberCoroutineScope()

    val deviceConfiguration = currentDeviceConfiguration()
    val isWideScreen = deviceConfiguration.isWideScreen
    val isMobile = deviceConfiguration.isMobile

    // Initialize to show list pane on start
    LaunchedEffect(Unit) {
        if (scaffoldNavigator.currentDestination == null) {
            scaffoldNavigator.navigateTo(ThreePaneScaffoldRole.Secondary)
        }
    }

    // Track detail pane state to ensure proper navigation
    LaunchedEffect(isDetailFlowActive, isTrackFlowActive, isWideScreen, deviceConfiguration) {
        val currentPaneRole = scaffoldNavigator.currentDestination?.pane

        // On wide screens (desktop): show 3 panes when all are active
        // On mobile/tablet: navigate between panes (only one visible at a time)
        val targetPaneRole = if (isWideScreen && deviceConfiguration == DeviceConfiguration.DESKTOP) {
            // On desktop, if track is active, focus on track (all 3 panes visible)
            // If detail is active (but no track), focus on detail (2 panes visible)
            // If neither, focus on list (list pane visible, detail pane hidden/empty)
            when {
                isTrackFlowActive -> ThreePaneScaffoldRole.Tertiary // Track (all 3 panes visible)
                isDetailFlowActive -> ThreePaneScaffoldRole.Primary // Detail (list + detail visible)
                else -> ThreePaneScaffoldRole.Secondary // List (only list visible)
            }
        } else {
            // On mobile/tablet, navigate between panes (only one visible)
            when {
                isTrackFlowActive -> ThreePaneScaffoldRole.Tertiary // Track
                isDetailFlowActive -> ThreePaneScaffoldRole.Primary // Detail (hides list)
                else -> ThreePaneScaffoldRole.Secondary // List (hides detail)
            }
        }

        // Always navigate to target pane to ensure correct state
        // This ensures when detail becomes empty, we navigate back to list
        scope.launch {
            scaffoldNavigator.navigateTo(targetPaneRole)
        }
    }

    BackHandler(enabled = (isMobile && scaffoldNavigator.canNavigateBack()) || isDetailFlowActive || isTrackFlowActive) {
        when {
            isTrackFlowActive -> {
                // Handle track pane back navigation
                if (trackNavController.previousBackStackEntry == null) {
                    trackNavController.navigate(Screen.App.EmptyDetailScreenDestination) {
                        popUpTo(trackNavController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                    // On mobile, navigate back to detail pane
                    if (isMobile && scaffoldNavigator.currentDestination?.pane == ThreePaneScaffoldRole.Tertiary) {
                        scope.launch { scaffoldNavigator.navigateTo(ThreePaneScaffoldRole.Primary) }
                    }
                } else {
                    trackNavController.popBackStack()
                }
            }
            isDetailFlowActive -> {
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
            }
            isMobile && scaffoldNavigator.canNavigateBack() -> {
                // On mobile, handle scaffold-level back navigation
                scope.launch { scaffoldNavigator.navigateBack() }
            }
        }
        // On wide screens, if no detail/track is active, system handles back (might close app)
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

    val navigationItems = remember {
        listOf(
            NavigationItem("Home", Icons.Default.Home, Screen.App.DashBoard.Home),
            NavigationItem("Search", Icons.Default.Search, Screen.App.DashBoard.Search),
            NavigationItem("Library", Icons.Default.LibraryMusic, Screen.App.DashBoard.Library),
            NavigationItem("Profile", Icons.Default.Person, Screen.App.DashBoard.Profile)
        )
    }

    val currentDestination by rememberSaveable { mutableStateOf(trackNavController.currentDestination?.route) }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.App.DashBoard.Home) }




    // NavigationSuiteScaffold with empty items - can be expanded later
    NavigationSuiteScaffold(
        navigationItems = {
            NavigationBar {
                navigationItems.forEach { destination ->
                    val isSelected = when (destination.screen) {
                        Screen.App.DashBoard.Home -> currentScreen is Screen.App.DashBoard.Home
                        Screen.App.DashBoard.Search -> currentScreen is Screen.App.DashBoard.Search ||
                                currentScreen is Screen.App.Track ||
                                currentScreen is Screen.App.Album ||
                                currentScreen is Screen.App.Artist ||
                                currentScreen is Screen.App.Playlist

                        Screen.App.DashBoard.Library -> currentScreen is Screen.App.DashBoard.Library
                        Screen.App.DashBoard.Profile -> currentScreen is Screen.App.DashBoard.Profile
                        else -> false
                    }
                    NavigationBarItem(
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                        selected = isSelected,
                        onClick = { currentScreen = destination.screen }
                    )
                }
            }
            // Navigation items can be added here when needed
        },

    ) {
        ListDetailPaneScaffold(
            directive = scaffoldDirective,
            value = scaffoldNavigator.scaffoldValue,
            listPane = {
                if (isMobile)
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
                    navController = detailNavController,
                    trackNavController = trackNavController,
                    listNavController = listNavController,
                    onTrackSelected = { trackId ->
                        // Navigate to track detail in the extra pane
                        trackNavController.navigate(Screen.App.Track(trackId)) {
                            launchSingleTop = true
                        }
                        // On desktop, ensure the scaffold navigates to Tertiary pane to show the track
                        if (isWideScreen && deviceConfiguration == DeviceConfiguration.DESKTOP) {
                            scope.launch {
                                scaffoldNavigator.navigateTo(ThreePaneScaffoldRole.Tertiary)
                            }
                        }
                    },
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
            extraPane = {
                // Third pane for track details
                TrackDetailPaneNavHost(
                    navController = trackNavController
                )
            }
        )
    }
}

@Composable
fun ListContent(
    navController: NavHostController,
    onNavigateToDetail: (Screen) -> Unit,
    onCurrentScreenChange: (Screen) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.App.DashBoard.Home,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<Screen.App.DashBoard.Home> {
            HomeScreen(
                onAlbumClick = { albumId ->
                    onNavigateToDetail(Screen.App.Album(albumId))
                },
                onPlaylistClick = { playlistId ->
                    onNavigateToDetail(Screen.App.Playlist(playlistId))
                }
            )
        }
        composable<Screen.App.DashBoard.Search> {
            SearchScreen(
                onTrackClick = { trackId ->
                    onNavigateToDetail(Screen.App.Track(trackId))
                },
                onAlbumClick = { albumId ->
                    onNavigateToDetail(Screen.App.Album(albumId))
                },
                onArtistClick = { artistId ->
                    onNavigateToDetail(Screen.App.Artist(artistId))
                },
                onPlaylistClick = { playlistId ->
                    onNavigateToDetail(Screen.App.Playlist(playlistId))
                }
            )
        }
        composable<Screen.App.DashBoard.Library> {
            LibraryScreen()
        }
        composable<Screen.App.DashBoard.Profile> {
            ProfileScreen()
        }
    }

    // Update current screen based on navigation
    val currentDestination by navController.currentBackStackEntryAsState()
    LaunchedEffect(currentDestination) {
        currentDestination?.destination?.route?.let { route ->
            when {
                route.contains("Home") -> onCurrentScreenChange(Screen.App.DashBoard.Home)
                route.contains("Search") -> onCurrentScreenChange(Screen.App.DashBoard.Search)
                route.contains("Library") -> onCurrentScreenChange(Screen.App.DashBoard.Library)
                route.contains("Profile") -> onCurrentScreenChange(Screen.App.DashBoard.Profile)
            }
        }
    }
}

@Composable
fun DetailPaneNavHost(
    navController: NavHostController,
    trackNavController: NavHostController,
    listNavController: NavHostController,
    onTrackSelected: (String) -> Unit,
    onNavigateToDetail: (Screen) -> Unit,
    onCurrentScreenChange: (Screen) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.App.EmptyDetailScreenDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<Screen.App.EmptyDetailScreenDestination> {
            // Should Return Either PlaylistDetail,ArtistDetails,AlbumDetails when navigated back
            HomeScreen(
                onAlbumClick = { albumId ->
                    onNavigateToDetail(Screen.App.Album(albumId))
                },
                onPlaylistClick = { playlistId ->
                    onNavigateToDetail(Screen.App.Playlist(playlistId))
                }
            )
        }
        composable<Screen.App.Album> { backStackEntry ->
            val album = backStackEntry.toRoute<Screen.App.Album>()
            AlbumScreen(
                albumId = album.albumId,
                onTrackSelected = onTrackSelected, // Pass to track detail pane
                onBack = {
                    // Navigate detail pane to empty screen
                    // Also navigate list pane back to Home if needed
                    navController.navigate(Screen.App.EmptyDetailScreenDestination) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Screen.App.Artist> { backStackEntry ->
            val artist = backStackEntry.toRoute<Screen.App.Artist>()
            ArtistScreen(
                artistId = artist.artistId,
                onTrackSelected = onTrackSelected, // Pass to track detail pane
                onBack = {
                    // Navigate detail pane to empty screen
                    // Also navigate list pane back to Home
                    navController.navigate(Screen.App.EmptyDetailScreenDestination) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Screen.App.Playlist> { backStackEntry ->
            val playlist = backStackEntry.toRoute<Screen.App.Playlist>()
            PlaylistScreen(
                playlistId = playlist.playlistId,
                onTrackSelected = onTrackSelected, // Pass to track detail pane
                onBack = {
                    // Navigate detail pane to empty screen
                    // Also navigate list pane back to Home
                    navController.navigate(Screen.App.EmptyDetailScreenDestination) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        // Track navigation from list pane goes here but opens in extra pane
        composable<Screen.App.Track> { backStackEntry ->
            val track = backStackEntry.toRoute<Screen.App.Track>()
            // If track is selected from list, navigate to track pane
            LaunchedEffect(track.trackId) {
                trackNavController.navigate(Screen.App.Track(track.trackId)) {
                    launchSingleTop = true
                }
                // Go back to empty detail
                navController.navigate(Screen.App.EmptyDetailScreenDestination) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    launchSingleTop = true
                }
            }
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}


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

private data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)