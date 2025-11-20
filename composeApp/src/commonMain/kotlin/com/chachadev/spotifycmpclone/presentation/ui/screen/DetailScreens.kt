package com.chachadev.spotifycmpclone.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chachadev.spotifycmpclone.domain.model.Album
import com.chachadev.spotifycmpclone.domain.model.Artist
import com.chachadev.spotifycmpclone.domain.model.Playlist
import com.chachadev.spotifycmpclone.domain.model.Track
import com.chachadev.spotifycmpclone.presentation.ui.component.CoilImage
import com.chachadev.spotifycmpclone.utils.ImageLoader
import com.chachadev.spotifycmpclone.presentation.ui.component.TrackItem
import com.chachadev.spotifycmpclone.presentation.viewmodel.AlbumDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.ArtistDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.PlaylistDetailViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.TrackDetailViewModel
import org.koin.compose.viewmodel.koinViewModel





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = content
    )
}

@Composable
fun DetailScreenBody(
    paddingValues: PaddingValues,
    isLoading: Boolean,
    error: String?,
    hasContent: Boolean,
    onRetry: () -> Unit,
    content: @Composable () -> Unit
) {
    val modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()

    when {
        isLoading && !hasContent -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        error != null && !hasContent -> {
            ErrorPlaceholder(
                modifier = modifier,
                message = error,
                onRetry = onRetry
            )
        }

        else -> {
            Column(modifier = modifier) {
                content()
            }
        }
    }
}

@Composable
fun AlbumDetailContent(
    album: Album?,
    tracks: List<Track>,
    isRefreshing: Boolean,
    error: String?,
    onRetry: () -> Unit,
    onTrackSelected: (String) -> Unit
) {
    DetailContentList(
        header = {
            album?.let {
                DetailHeader(
                    title = it.name,
                    subtitle = it.artists.joinToString { artist -> artist.name },
                    metadata = listOfNotNull(
                        it.releaseDate,
                        it.totalTracks?.let { total -> "$total tracks" }
                    ).takeIf { meta -> meta.isNotEmpty() }?.joinToString(separator = " • "),
                    imageUrl = it.images.firstOrNull()?.url
                )
            }
        },
        sectionTitle = "Tracks",
        emptyMessage = "No tracks available for this album.",
        tracks = tracks,
        isRefreshing = isRefreshing,
        error = error,
        onRetry = onRetry,
        onTrackSelected = onTrackSelected
    )
}

@Composable
fun ArtistDetailContent(
    artist: Artist?,
    tracks: List<Track>,
    isRefreshing: Boolean,
    error: String?,
    onRetry: () -> Unit,
    onTrackSelected: (String) -> Unit
) {
    DetailContentList(
        header = {
            artist?.let {
                DetailHeader(
                    title = it.name,
                    subtitle = it.genres.takeIf { genres -> genres.isNotEmpty() }?.joinToString(),
                    metadata = it.followers?.let { followers -> "${followers} followers" },
                    imageUrl = it.images.firstOrNull()?.url
                )
            }
        },
        sectionTitle = "Top Tracks",
        emptyMessage = "Top tracks for this artist could not be loaded.",
        tracks = tracks,
        isRefreshing = isRefreshing,
        error = error,
        onRetry = onRetry,
        onTrackSelected = onTrackSelected
    )
}

@Composable
fun PlaylistDetailContent(
    playlist: Playlist?,
    tracks: List<Track>,
    isRefreshing: Boolean,
    error: String?,
    onRetry: () -> Unit,
    onTrackSelected: (String) -> Unit
) {
    DetailContentList(
        header = {
            playlist?.let {
                DetailHeader(
                    title = it.name,
                    subtitle = it.description,
                    metadata = listOfNotNull(
                        it.owner?.displayName,
                        it.tracks?.total?.let { total -> "$total tracks" }
                    ).takeIf { meta -> meta.isNotEmpty() }?.joinToString(separator = " • "),
                    imageUrl = it.images.firstOrNull()?.url
                )
            }
        },
        sectionTitle = "Tracks",
        emptyMessage = "This playlist does not have tracks we can display right now.",
        tracks = tracks,
        isRefreshing = isRefreshing,
        error = error,
        onRetry = onRetry,
        onTrackSelected = onTrackSelected
    )
}

@Composable
private fun DetailContentList(
    header: (@Composable () -> Unit)?,
    sectionTitle: String,
    emptyMessage: String,
    tracks: List<Track>,
    isRefreshing: Boolean,
    error: String?,
    onRetry: () -> Unit,
    onTrackSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isRefreshing) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            header?.let {
                item { it() }
            }

            error?.let { message ->
                item {
                    ErrorBanner(
                        message = message,
                        onRetry = onRetry
                    )
                }
            }

            item {
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (tracks.isEmpty()) {
                item {
                    Text(
                        text = emptyMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(tracks) { track ->
                    TrackItem(
                        track = track,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onTrackSelected(track.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TrackDetailContent(
    track: Track?,
    isRefreshing: Boolean,
    error: String?,
    onRetry: () -> Unit
) {
    if (track == null) return

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isRefreshing) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DetailHeader(
                    title = track.name,
                    subtitle = track.artists.joinToString { it.name },
                    metadata = listOfNotNull(
                        formatDuration(track.durationMs),
                        track.album?.name
                    ).takeIf { it.isNotEmpty() }?.joinToString(" • "),
                    imageUrl = track.images.firstOrNull()?.url ?: track.album?.images?.firstOrNull()?.url
                )
            }

            error?.let { message ->
                item {
                    ErrorBanner(
                        message = message,
                        onRetry = onRetry
                    )
                }
            }

            item {
                TrackInfoCard(track = track)
            }

            track.album?.let { album ->
                item {
                    AlbumSummaryCard(album = album)
                }
            }
        }
    }
}

@Composable
private fun TrackInfoCard(track: Track) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Track details",
                style = MaterialTheme.typography.titleMedium
            )
            InfoRow(label = "Duration", value = formatDuration(track.durationMs))
            track.album?.name?.let {
                InfoRow(label = "Album", value = it)
            }
            track.previewUrl?.let {
                InfoRow(label = "Preview URL", value = it)
            }
            track.externalUrls?.spotify?.let {
                InfoRow(label = "Spotify Link", value = it)
            }
        }
    }
}

@Composable
private fun AlbumSummaryCard(album: Album) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Album",
                style = MaterialTheme.typography.titleMedium
            )
            CoilImage(
                imageUrl = album.images.firstOrNull()?.url,
                contentDescription = album.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = album.name,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = album.artists.joinToString { it.name },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private fun formatDuration(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val secondsPart = seconds.toString().padStart(2, '0')
    return "${minutes}:${secondsPart}"
}

@Composable
private fun DetailHeader(
    title: String,
    subtitle: String?,
    metadata: String?,
    imageUrl: String?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CoilImage(
            imageUrl = imageUrl,
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
            subtitle?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            metadata?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ErrorBanner(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun ErrorPlaceholder(
    modifier: Modifier,
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun DetailPlaceholderScreen(
    title: String,
    message: String,
    description: String,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        title = title,
        onBack = onBack
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
