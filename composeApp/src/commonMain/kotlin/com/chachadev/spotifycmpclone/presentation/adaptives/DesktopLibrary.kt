package com.chachadev.spotifycmpclone.presentation.adaptives

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chachadev.core.domain.model.Playlist
import com.chachadev.core.domain.model.Album
import com.chachadev.core.domain.model.Artist
import com.chachadev.core.domain.model.PlaylistItem
import com.chachadev.spotifycmpclone.presentation.ui.component.CoilImage
import com.chachadev.spotifycmpclone.presentation.viewmodel.LibraryViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopLibrary(
    viewModel: LibraryViewModel = koinViewModel(),
    onAlbumClick: (String) -> Unit = {},
    onPlaylistClick: (String) -> Unit = {},
    onArtistClick: (String) -> Unit = {},
    onShowClick: (String) -> Unit = {},
    onEpisodeClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var selectedFilter by remember { mutableStateOf(LibraryFilter.Playlists) }
    var searchQuery by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf(SortOption.Recents) }
    
    // Convert PlaylistItem to Playlist for compatibility
    val playlists = remember(uiState.playlists) {
        uiState.playlists.mapNotNull { playlistItem ->
            // Convert PlaylistItem to Playlist
            val id = playlistItem.id ?: return@mapNotNull null
            val name = playlistItem.name ?: return@mapNotNull null
            Playlist(
                id = id,
                name = name,
                description = playlistItem.description,
                images = playlistItem.images ?: emptyList(),
                owner = playlistItem.owner?.let { owner ->
                    com.chachadev.core.domain.model.PlaylistOwner(
                        id = owner.id ?: "",
                        displayName = owner.displayName
                    )
                },
                tracks = playlistItem.tracks?.let { tracks ->
                    com.chachadev.core.domain.model.PlaylistTracks(
                        total = tracks.total ?: 0
                    )
                },
                externalUrls = playlistItem.externalUrls
            )
        }
    }
    
    val albums = remember(uiState.savedAlbums) {
        uiState.savedAlbums
    }
    
    // Extract unique artists from saved albums
    val artists = remember(albums) {
        albums.flatMap { album ->
            album.artists
        }.distinctBy { it.id }
    }
    
    // Get shows (podcasts) and episodes (downloads) from ViewModel
    val shows = remember(uiState.savedShows) {
        uiState.savedShows
    }
    
    val episodes = remember(uiState.savedEpisodes) {
        uiState.savedEpisodes
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            LibraryHeader(
                onCreateClick = { /* Handle create */ }
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
            
            // Loading and Error States
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                uiState.error?.let { error ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "Error: $error",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                
                // Sign in prompt
                if (uiState.requiresSignIn) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Sign in to view your library",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            text = "Your saved albums, playlists, shows, and episodes will appear here once you sign in with Spotify.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    // Content List
                    LibraryContentList(
                        filter = selectedFilter,
                        searchQuery = searchQuery,
                        playlists = playlists,
                        albums = albums,
                        artists = artists,
                        shows = shows,
                        episodes = episodes,
                        onAlbumClick = onAlbumClick,
                        onPlaylistClick = onPlaylistClick,
                        onArtistClick = onArtistClick,
                        onShowClick = onShowClick,
                        onEpisodeClick = onEpisodeClick
                    )
                }
            }
        }
    }
}

enum class LibraryFilter {
    Playlists, Podcasts, Albums, Artists, Downloads
}

enum class SortOption {
    Recents, RecentlyAdded, Alphabetical, Creator
}

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

@Composable
private fun LibraryContentList(
    filter: LibraryFilter,
    searchQuery: String,
    playlists: List<Playlist>,
    albums: List<Album>,
    artists: List<Artist>,
    shows: List<com.chachadev.core.domain.model.Show>,
    episodes: List<com.chachadev.core.domain.model.Episode>,
    onAlbumClick: (String) -> Unit,
    onPlaylistClick: (String) -> Unit,
    onArtistClick: (String) -> Unit,
    onShowClick: (String) -> Unit,
    onEpisodeClick: (String) -> Unit
) {
    val filteredItems = remember(filter, searchQuery, playlists, albums, artists, shows, episodes) {
        val items = when (filter) {
            LibraryFilter.Playlists -> playlists.map { LibraryItem.Playlist(it) }
            LibraryFilter.Albums -> albums.map { LibraryItem.Album(it) }
            LibraryFilter.Artists -> artists.map { LibraryItem.Artist(it) }
            LibraryFilter.Podcasts -> shows.mapNotNull { show ->
                show.id?.let { LibraryItem.Show(show) }
            }
            LibraryFilter.Downloads -> episodes.mapNotNull { episode ->
                episode.id?.let { LibraryItem.Episode(episode) }
            }
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
                    is LibraryItem.Show -> it.show.name?.contains(searchQuery, ignoreCase = true) == true ||
                            it.show.publisher?.contains(searchQuery, ignoreCase = true) == true
                    is LibraryItem.Episode -> it.episode.name?.contains(searchQuery, ignoreCase = true) == true ||
                            it.episode.show?.name?.contains(searchQuery, ignoreCase = true) == true
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
                is LibraryItem.Show -> "show_${item.show.id}"
                is LibraryItem.Episode -> "episode_${item.episode.id}"
            }}
        ) { item ->
            LibraryItemRow(
                item = item,
                onAlbumClick = onAlbumClick,
                onPlaylistClick = onPlaylistClick,
                onArtistClick = onArtistClick,
                onShowClick = onShowClick,
                onEpisodeClick = onEpisodeClick
            )
        }
    }
}

private sealed class LibraryItem {
    data class Playlist(val playlist: com.chachadev.core.domain.model.Playlist) : LibraryItem()
    data class Album(val album: com.chachadev.core.domain.model.Album) : LibraryItem()
    data class Artist(val artist: com.chachadev.core.domain.model.Artist) : LibraryItem()
    data class Show(val show: com.chachadev.core.domain.model.Show) : LibraryItem()
    data class Episode(val episode: com.chachadev.core.domain.model.Episode) : LibraryItem()
}

@Composable
private fun LibraryItemRow(
    item: LibraryItem,
    onAlbumClick: (String) -> Unit,
    onPlaylistClick: (String) -> Unit,
    onArtistClick: (String) -> Unit,
    onShowClick: (String) -> Unit,
    onEpisodeClick: (String) -> Unit
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
        is LibraryItem.Show -> {
            val imageUrl = item.show.images?.firstOrNull()?.url
            val subtitle = buildString {
                append("Podcast")
                item.show.publisher?.let { publisher ->
                    append(" • $publisher")
                }
                item.show.totalEpisodes?.let { total ->
                    append(" • $total episodes")
                }
            }
            val showId = item.show.id ?: ""
            Quadruple(imageUrl, item.show.name ?: "Unknown Show", subtitle) {
                if (showId.isNotEmpty()) {
                    onShowClick(showId)
                }
            }
        }
        is LibraryItem.Episode -> {
            val imageUrl = item.episode.images?.firstOrNull()?.url ?: item.episode.show?.images?.firstOrNull()?.url
            val subtitle = buildString {
                append("Episode")
                item.episode.show?.name?.let { showName ->
                    append(" • $showName")
                }
                item.episode.releaseDate?.let { date ->
                    append(" • $date")
                }
            }
            val episodeId = item.episode.id ?: ""
            Quadruple(imageUrl, item.episode.name ?: "Unknown Episode", subtitle) {
                if (episodeId.isNotEmpty()) {
                    onEpisodeClick(episodeId)
                }
            }
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
        CoilImage(
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
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

