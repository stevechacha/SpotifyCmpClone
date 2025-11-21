package com.chachadev.spotifycmpclone.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.chachadev.core.common.screen.ScreenOrientation
import com.chachadev.spotifycmpclone.presentation.viewmodel.ArtistDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ArtistScreen(
    artistId: String,
    orientation: ScreenOrientation,
    viewModel: ArtistDetailViewModel = koinViewModel(),
    onTrackSelected: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.load(artistId)
    }

    DetailScreenScaffold(
        title = uiState.artist?.name ?: "Artist",
        onBack = onBack,
        orientation = orientation
    ) { paddingValues ->
        DetailScreenBody(
            paddingValues = paddingValues,
            isLoading = uiState.isLoading && uiState.topTracks.isEmpty() && uiState.artist == null,
            error = uiState.error,
            hasContent = uiState.artist != null || uiState.topTracks.isNotEmpty(),
            onRetry = { viewModel.refresh() }
        ) {
            ArtistDetailContent(
                artist = uiState.artist,
                tracks = uiState.topTracks,
                isRefreshing = uiState.isLoading && uiState.topTracks.isNotEmpty(),
                error = uiState.error,
                onRetry = { viewModel.refresh() },
                onTrackSelected = onTrackSelected
            )
        }
    }
}