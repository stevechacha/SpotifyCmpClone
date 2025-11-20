package com.chachadev.spotifycmpclone.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
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
import com.chachadev.spotifycmpclone.utils.createNoSpacingPaneScaffoldDirective
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
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

    val windowSizeClass = calculateWindowSizeClass()

    LaunchedEffect(isDetailFlowActive, scaffoldNavigator.currentDestination, windowSizeClass.widthSizeClass) {
        val currentPaneRole = scaffoldNavigator.currentDestination?.pane
        val targetPaneRole = if (isDetailFlowActive) {
            ThreePaneScaffoldRole.Primary // Detail
        } else {
            ThreePaneScaffoldRole.Secondary // List
        }

        if (currentPaneRole != targetPaneRole) {
            scaffoldNavigator.navigateTo(targetPaneRole)
        }
    }

    BackHandler(enabled = scaffoldNavigator.canNavigateBack() || isDetailFlowActive) {
        if (isDetailFlowActive) {
            if (detailNavController.previousBackStackEntry == null) {
                // Navigate detailNavController to EmptyDetailScreenDestination
                detailNavController.navigate(Screen.App.EmptyDetailScreenDestination) {
                    popUpTo(detailNavController.graph.findStartDestination().id) { inclusive = true }
                    launchSingleTop = true
                }
            } else {
                detailNavController.popBackStack()
            }
        } else if (scaffoldNavigator.canNavigateBack()) {
            scope.launch { scaffoldNavigator.navigateBack() }
        }
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

    NavigationSuiteScaffold(
        navigationItems = {}

    ) {
        ListDetailPaneScaffold(
            directive = scaffoldNavigator.scaffoldDirective,
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
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.App.EmptyDetailScreenDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<Screen.App.EmptyDetailScreenDestination> {
            // Empty detail screen - shows nothing when no detail is selected
            Box(
                modifier = Modifier.fillMaxSize()
            )
        }
        composable<Screen.App.Track> { backStackEntry ->
            val track = backStackEntry.toRoute<Screen.App.Track>()
            TrackScreen(
                trackId = track.trackId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.App.Album> { backStackEntry ->
            val album = backStackEntry.toRoute<Screen.App.Album>()
            AlbumScreen(
                albumId = album.albumId,
                onTrackSelected = { trackId ->
                    navController.navigate(Screen.App.Track(trackId))
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.App.Artist> { backStackEntry ->
            val artist = backStackEntry.toRoute<Screen.App.Artist>()
            ArtistScreen(
                artistId = artist.artistId,
                onTrackSelected = { trackId ->
                    navController.navigate(Screen.App.Track(trackId))
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.App.Playlist> { backStackEntry ->
            val playlist = backStackEntry.toRoute<Screen.App.Playlist>()
            PlaylistScreen(
                playlistId = playlist.playlistId,
                onTrackSelected = { trackId ->
                    navController.navigate(Screen.App.Track(trackId))
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}