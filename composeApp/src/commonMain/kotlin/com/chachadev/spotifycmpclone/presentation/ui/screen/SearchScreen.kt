package com.chachadev.spotifycmpclone.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chachadev.spotifycmpclone.presentation.ui.component.AlbumCard
import com.chachadev.spotifycmpclone.presentation.ui.component.ArtistCard
import com.chachadev.spotifycmpclone.presentation.ui.component.PlaylistCard
import com.chachadev.spotifycmpclone.presentation.ui.component.TrackItem
import com.chachadev.spotifycmpclone.presentation.viewmodel.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    onTrackClick: (String) -> Unit = {},
    onAlbumClick: (String) -> Unit = {},
    onArtistClick: (String) -> Unit = {},
    onPlaylistClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue
                viewModel.search(newValue)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search for songs, artists, albums...") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                )
            }
            uiState.error != null -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Error: ${uiState.error}",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            uiState.searchResult != null -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    uiState.searchResult?.tracks?.items?.takeIf { it.isNotEmpty() }?.let { tracks ->
                        item {
                            Text(
                                text = "Tracks",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        items(tracks) { track ->
                            TrackItem(
                                track = track,
                                onClick = { onTrackClick(track.id) }
                            )
                        }
                    }

                    uiState.searchResult?.albums?.items?.takeIf { it.isNotEmpty() }?.let { albums ->
                        item {
                            Text(
                                text = "Albums",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(albums) { album ->
                                    AlbumCard(
                                        album = album,
                                        onClick = { onAlbumClick(album.id) }
                                    )
                                }
                            }
                        }
                    }

                    uiState.searchResult?.artists?.items?.takeIf { it.isNotEmpty() }?.let { artists ->
                        item {
                            Text(
                                text = "Artists",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(artists) { artist ->
                                    ArtistCard(
                                        artist = artist,
                                        onClick = { onArtistClick(artist.id) }
                                    )
                                }
                            }
                        }
                    }

                    uiState.searchResult?.playlists?.items?.takeIf { it.isNotEmpty() }?.let { playlists ->
                        item {
                            Text(
                                text = "Playlists",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(playlists) { playlist ->
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
            else -> {
                Text(
                    text = "Start typing to search...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

