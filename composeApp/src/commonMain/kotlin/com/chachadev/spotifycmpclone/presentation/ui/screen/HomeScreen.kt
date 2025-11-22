package com.chachadev.spotifycmpclone.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chachadev.spotifycmpclone.presentation.ui.component.AlbumCard
import com.chachadev.spotifycmpclone.presentation.ui.component.PlaylistCard
import com.chachadev.spotifycmpclone.presentation.ui.component.TrackCard
import com.chachadev.spotifycmpclone.presentation.ui.component.TrackItem
import com.chachadev.spotifycmpclone.presentation.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onAlbumClick: (String) -> Unit = {},
    onPlaylistClick: (String) -> Unit = {},
    onTrackClick: (String) -> Unit = {}
)  = Box(modifier = Modifier.fillMaxSize()){
    val uiState by viewModel.uiState.collectAsState()
    
    // Debug: Log state changes - track all state properties
    LaunchedEffect(uiState.recentlyPlayedTracks.size, uiState.isLoading, uiState.error) {
        println("HomeScreen: UI State updated - recentlyPlayedTracks: ${uiState.recentlyPlayedTracks.size}, isLoading: ${uiState.isLoading}, error: ${uiState.error}")
        println("HomeScreen: First track: ${uiState.recentlyPlayedTracks.firstOrNull()?.name}")
    }
    
    // Log when composable recomposes
    println("HomeScreen: Composable recomposed - recentlyPlayedTracks: ${uiState.recentlyPlayedTracks.size}")

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = 32.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (uiState.isLoading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                )
            }
        } else {
            uiState.error?.let { error ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
            }

            // Show Recently Played section FIRST
            if (uiState.recentlyPlayedTracks.isNotEmpty()) {
                item {
                    Text(
                        text = "Recently Played",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(
                            items = uiState.recentlyPlayedTracks,
                            key = { index, track -> "${track.id}-$index" }
                        ) { _, track ->
                            TrackCard(
                                track = track,
                                onClick = { onTrackClick(track.id) }
                            )
                        }
                    }
                }
            } else {
                // Show message if no recently played tracks
                item {
                    Text(
                        text = "No recently played tracks. Play some music on Spotify to see them here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

             //Temporarily commented out to test if Recently Played is showing
            if (uiState.newReleases.isNotEmpty()) {
                item {
                    Text(
                        text = "New Releases",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.newReleases,
                            key = { album -> album.id }
                        ) { album ->
                            AlbumCard(
                                album = album,
                                onClick = { onAlbumClick(album.id) }
                            )
                        }
                    }
                }
            }

            if (uiState.featuredPlaylists.isNotEmpty()) {
                item {
                    Text(
                        text = "Featured Playlists",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.featuredPlaylists,
                            key = { playlist -> playlist.id }
                        ) { playlist ->
                            PlaylistCard(
                                playlist = playlist,
                                onClick = { onPlaylistClick(playlist.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

