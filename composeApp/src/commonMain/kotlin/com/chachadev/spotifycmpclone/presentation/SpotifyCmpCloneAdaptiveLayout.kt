package com.chachadev.spotifycmpclone.presentation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import com.chachadev.spotifycmpclone.utils.DeviceConfiguration
import com.chachadev.spotifycmpclone.utils.createNoSpacingPaneScaffoldDirective
import com.chachadev.spotifycmpclone.utils.currentDeviceConfiguration
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SpotifyCmpCloneAdaptiveLayout() = Column{
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

    BackHandler(enabled = (isMobile || scaffoldNavigator.canNavigateBack()) || isDetailFlowActive || isTrackFlowActive) {
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

    val navigationItems = remember {
        listOf(
            NavigationItem("Home", Icons.Default.Home, Screen.App.DashBoard.Home),
            NavigationItem("Search", Icons.Default.Search, Screen.App.DashBoard.Search),
            NavigationItem("Library", Icons.Default.LibraryMusic, Screen.App.DashBoard.Library),
            NavigationItem("Profile", Icons.Default.Person, Screen.App.DashBoard.Profile)
        )
    }

    val shouldShowNavigationBar = !isDetailFlowActive && !isTrackFlowActive

    if (isMobile){
        Text(
            "hellow World",
            fontSize = 56.sp
        )
    }








    // NavigationSuiteScaffold with empty items - can be expanded later
    NavigationSuiteScaffold(
        navigationItems = {
            if (shouldShowNavigationBar && isMobile) {
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
/*
@Composable
fun DesktopLibrary(
    onAlbumClick: (String) -> Unit = {},
    onPlaylistClick: (String) -> Unit = {},
    onArtistClick: (String) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf(LibraryFilter.Playlists) }
    var searchQuery by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf(SortOption.Recents) }
    
    // Mock data - replace with actual ViewModel/Repository calls
    val playlists = remember { emptyList<com.chachadev.spotifycmpclone.domain.model.Playlist>() }
    val albums = remember { emptyList<com.chachadev.spotifycmpclone.domain.model.Album>() }
    val artists = remember { emptyList<com.chachadev.spotifycmpclone.domain.model.Artist>() }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            LibraryHeader(
                onCreateClick = { *//* Handle create *//* }
            )
            
            // Filter Bar
            LibraryFilterBar(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )
            
            // Search and Sort Bar
            LibrarySearchSortBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                sortOption = sortOption,
                onSortOptionChange = { sortOption = it }
            )
            
            // Content List
            LibraryContentList(
                filter = selectedFilter,
                searchQuery = searchQuery,
                playlists = playlists,
                albums = albums,
                artists = artists,
                onAlbumClick = onAlbumClick,
                onPlaylistClick = onPlaylistClick,
                onArtistClick = onArtistClick
            )
        }
    }
}*/

 /*enum class LibraryFilter {
    Playlists, Podcasts, Albums, Artists, Downloads
}

enum class SortOption {
    Recents, RecentlyAdded, Alphabetical, Creator
}*/

@Composable
private fun LibraryHeader(
    onCreateClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Menu icon placeholder
            IconButton(onClick = { /* Menu */ }) {
                Icon(
                    imageVector = Icons.Default.Search, // Replace with menu icon
                    contentDescription = "Menu"
                )
            }
            Text(
                text = "Your Library",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(onClick = onCreateClick) {
                Text("+ Create")
            }
            // Full-screen toggle placeholder
            IconButton(onClick = { /* Toggle */ }) {
                Icon(
                    imageVector = Icons.Default.Search, // Replace with fullscreen icon
                    contentDescription = "Fullscreen"
                )
            }
        }
    }
}

@Composable
private fun LibraryFilterBar(
    selectedFilter: LibraryFilter,
    onFilterSelected: (LibraryFilter) -> Unit
) {
    val filters = listOf(
        LibraryFilter.Playlists,
        LibraryFilter.Podcasts,
        LibraryFilter.Albums,
        LibraryFilter.Artists,
        LibraryFilter.Downloads
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter.name) }
            )
        }
    }
}

@Composable
private fun LibrarySearchSortBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    sortOption: SortOption,
    onSortOptionChange: (SortOption) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search icon placeholder
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier.size(20.dp)
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sortOption.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            // Sort icon placeholder
            Icon(
                imageVector = Icons.Default.Search, // Replace with list/sort icon
                contentDescription = "Sort",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
/*
@Composable
private fun LibraryContentList(
    filter: LibraryFilter,
    searchQuery: String,
    playlists: List<com.chachadev.spotifycmpclone.domain.model.Playlist>,
    albums: List<com.chachadev.spotifycmpclone.domain.model.Album>,
    artists: List<com.chachadev.spotifycmpclone.domain.model.Artist>,
    onAlbumClick: (String) -> Unit,
    onPlaylistClick: (String) -> Unit,
    onArtistClick: (String) -> Unit
) {
    val filteredItems = remember(filter, searchQuery, playlists, albums, artists) {
        val items = when (filter) {
            LibraryFilter.Playlists -> playlists.map { LibraryItem.Playlist(it) }
            LibraryFilter.Albums -> albums.map { LibraryItem.Album(it) }
            LibraryFilter.Artists -> artists.map { LibraryItem.Artist(it) }
            LibraryFilter.Podcasts -> emptyList() // TODO: Add podcast support
            LibraryFilter.Downloads -> emptyList() // TODO: Add downloads support
        }
        
        if (searchQuery.isBlank()) {
            items
        } else {
            items.filter {
                when (it) {
                    is LibraryItem.Playlist -> it.playlist.name.contains(searchQuery, ignoreCase = true)
                    is LibraryItem.Album -> it.album.name.contains(searchQuery, ignoreCase = true) ||
                            it.album.artists.any { artist -> artist.name.contains(searchQuery, ignoreCase = true) }
                    is LibraryItem.Artist -> it.artist.name.contains(searchQuery, ignoreCase = true)
                    is LibraryItem.Podcast -> false // TODO
                }
            }
        }
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = filteredItems,
            key = { item -> when (item) {
                is LibraryItem.Playlist -> "playlist_${item.playlist.id}"
                is LibraryItem.Album -> "album_${item.album.id}"
                is LibraryItem.Artist -> "artist_${item.artist.id}"
                is LibraryItem.Podcast -> "podcast_${item.id}"
            }}
        ) { item ->
            LibraryItemRow(
                item = item,
                onAlbumClick = onAlbumClick,
                onPlaylistClick = onPlaylistClick,
                onArtistClick = onArtistClick
            )
        }
    }
}*/

/* sealed class LibraryItem {
    data class Playlist(val playlist: com.chachadev.spotifycmpclone.domain.model.Playlist) : LibraryItem()
    data class Album(val album: com.chachadev.spotifycmpclone.domain.model.Album) : LibraryItem()
    data class Artist(val artist: com.chachadev.spotifycmpclone.domain.model.Artist) : LibraryItem()
    data class Podcast(val id: String, val name: String) : LibraryItem() // Placeholder
}*/
/*
@Composable
private fun LibraryItemRow(
    item: LibraryItem,
    onAlbumClick: (String) -> Unit,
    onPlaylistClick: (String) -> Unit,
    onArtistClick: (String) -> Unit
) {
    val (imageUrl, title, subtitle, onClick) = when (item) {
        is LibraryItem.Playlist -> {
            val imageUrl = item.playlist.images.firstOrNull()?.url
            val subtitle = buildString {
                append("Playlist")
                item.playlist.tracks?.total?.let { total ->
                    append(" • $total songs")
                }
                item.playlist.owner?.displayName?.let { owner ->
                    append(" • $owner")
                }
            }
            Quadruple(imageUrl, item.playlist.name, subtitle) {
                onPlaylistClick(item.playlist.id)
            }
        }
        is LibraryItem.Album -> {
            val imageUrl = item.album.images.firstOrNull()?.url
            val subtitle = buildString {
                append("Album")
                item.album.artists.firstOrNull()?.name?.let { artist ->
                    append(" • $artist")
                }
            }
            Quadruple(imageUrl, item.album.name, subtitle) {
                onAlbumClick(item.album.id)
            }
        }
        is LibraryItem.Artist -> {
            val imageUrl = item.artist.images.firstOrNull()?.url
            Quadruple(imageUrl, item.artist.name, "Artist") {
                onArtistClick(item.artist.id)
            }
        }
        is LibraryItem.Podcast -> {
            Quadruple(null, item.name, "Podcast") { *//* TODO *//* }
        }
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Artwork
        com.chachadev.spotifycmpclone.presentation.ui.component.CoilImage(
            imageUrl = imageUrl,
            contentDescription = title,
            modifier = Modifier.size(56.dp),
            contentScale = ContentScale.Crop
        )
        
        // Title and Subtitle
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}*/

/*
data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

*/




private data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)