package com.chachadev.spotifycmpclone.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chachadev.spotifycmpclone.presentation.ui.component.CoilImage
import androidx.navigation.NavGraph.Companion.findStartDestination
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

    BackHandler(enabled = (orientation is Portrait || scaffoldNavigator.canNavigateBack()) || isDetailFlowActive || isTrackFlowActive) {
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

    val navigationItems = remember {
        listOf(
            NavigationItem("Home", Icons.Default.Home, Screen.App.DashBoard.Home),
            NavigationItem("Search", Icons.Default.Search, Screen.App.DashBoard.Search),
            NavigationItem("Library", Icons.Default.LibraryMusic, Screen.App.DashBoard.Library),
            NavigationItem("Profile", Icons.Default.Person, Screen.App.DashBoard.Profile)
        )
    }

    val shouldShowNavigationBar = !isDetailFlowActive && !isTrackFlowActive

    if (orientation is Landscape){
        // Top Navigation Bar for Landscape
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Navigation arrows
            IconButton(
                onClick = { /* Handle back navigation */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            IconButton(
                onClick = { /* Handle forward navigation */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Forward",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Home button
            IconButton(
                onClick = {
                    currentListScreen = Screen.App.DashBoard.Home
                    listNavController.navigate(Screen.App.DashBoard.Home) {
                        popUpTo(listNavController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Search bar
            // Navigate to Search screen when user starts typing
            LaunchedEffect(sharedSearchQuery) {
                if (sharedSearchQuery.isNotEmpty() && currentListScreen != Screen.App.DashBoard.Search) {
                    currentListScreen = Screen.App.DashBoard.Search
                    listNavController.navigate(Screen.App.DashBoard.Search) {
                        popUpTo(listNavController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            }
            
            OutlinedTextField(
                value = sharedSearchQuery,
                onValueChange = { 
                    sharedSearchQuery = it
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                placeholder = {
                    Text(
                        text = "What do you want to play?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                // Navigate to Search screen when search icon is clicked
                                currentListScreen = Screen.App.DashBoard.Search
                                listNavController.navigate(Screen.App.DashBoard.Search) {
                                    popUpTo(listNavController.graph.findStartDestination().id) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            },
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Notification bell with badge
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = Color(0xFF1DB954) // Spotify green
                    )
                }
            ) {
                IconButton(
                    onClick = { /* Handle notifications */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            
            // Friends icon (using Person icon as placeholder)
            IconButton(
                onClick = { /* Handle friends */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Friends",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            // User profile picture
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable { 
                        currentListScreen = Screen.App.DashBoard.Profile
                        listNavController.navigate(Screen.App.DashBoard.Profile) {
                            popUpTo(listNavController.graph.findStartDestination().id) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
            ) {
                CoilImage(
                    imageUrl = null, // TODO: Get from ProfileViewModel
                    contentDescription = "Profile",
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
        }
    }
    // NavigationSuiteScaffold with empty items - can be expanded later
    NavigationSuiteScaffold(
        navigationItems = {
            if (shouldShowNavigationBar && orientation is Portrait) {
                NavigationBar {
                    navigationItems.forEach { destination ->
                        val isSelected = when (destination.screen) {
                            Screen.App.DashBoard.Home -> currentListScreen is Screen.App.DashBoard.Home
                            Screen.App.DashBoard.Search -> currentListScreen is Screen.App.DashBoard.Search
                            Screen.App.DashBoard.Library -> currentListScreen is Screen.App.DashBoard.Library
                            Screen.App.DashBoard.Profile -> currentListScreen is Screen.App.DashBoard.Profile
                            else -> false
                        }
                        NavigationBarItem(
                            icon = { Icon(destination.icon, contentDescription = destination.label) },
                            label = { Text(destination.label) },
                            selected = isSelected,
                            onClick = {
                                if (currentListScreen != destination.screen) {
                                    currentListScreen = destination.screen
                                    when (destination.screen) {
                                        Screen.App.DashBoard.Home -> {
                                            listNavController.navigate(Screen.App.DashBoard.Home) {
                                                popUpTo(listNavController.graph.findStartDestination().id) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                        Screen.App.DashBoard.Search -> {
                                            listNavController.navigate(Screen.App.DashBoard.Search) {
                                                popUpTo(listNavController.graph.findStartDestination().id) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                        Screen.App.DashBoard.Library -> {
                                            listNavController.navigate(Screen.App.DashBoard.Library) {
                                                popUpTo(listNavController.graph.findStartDestination().id) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                        Screen.App.DashBoard.Profile -> {
                                            listNavController.navigate(Screen.App.DashBoard.Profile) {
                                                popUpTo(listNavController.graph.findStartDestination().id) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                        else -> Unit
                                    }
                                }
                            }
                        )
                    }
                }
            }
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
                        detailNavController.navigate(destination) {
                            launchSingleTop = true
                        }
                    },
                    onCurrentScreenChange = { screen ->
                        currentListScreen = screen
                    },
                    initialSearchQuery = sharedSearchQuery,
                    orientation = orientation
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
                        detailNavController.navigate(destination) {
                            launchSingleTop = true
                        }
                    },
                    onCurrentScreenChange = { screen ->
                        currentListScreen = screen
                    },
                    orientation = orientation,
                    searchQuery = sharedSearchQuery
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


private data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)