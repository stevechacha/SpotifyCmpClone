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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chachadev.spotifycmpclone.domain.model.Playlist
import com.chachadev.spotifycmpclone.domain.model.Album
import com.chachadev.spotifycmpclone.domain.model.Artist
import com.chachadev.spotifycmpclone.presentation.ui.component.CoilImage

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
    val playlists = remember { emptyList<Playlist>() }
    val albums = remember { emptyList<Album>() }
    val artists = remember { emptyList<Artist>() }
    
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
}

private sealed class LibraryItem {
    data class Playlist(val playlist: com.chachadev.spotifycmpclone.domain.model.Playlist) : LibraryItem()
    data class Album(val album: com.chachadev.spotifycmpclone.domain.model.Album) : LibraryItem()
    data class Artist(val artist: com.chachadev.spotifycmpclone.domain.model.Artist) : LibraryItem()
    data class Podcast(val id: String, val name: String) : LibraryItem() // Placeholder
}

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
            Quadruple(null, item.name, "Podcast") { /* TODO */ }
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

