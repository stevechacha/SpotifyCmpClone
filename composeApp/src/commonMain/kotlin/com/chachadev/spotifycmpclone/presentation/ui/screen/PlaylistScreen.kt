package com.chachadev.spotifycmpclone.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.chachadev.core.common.screen.ScreenOrientation
import com.chachadev.spotifycmpclone.presentation.viewmodel.PlaylistDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlaylistScreen(
    playlistId: String,
    orientation: ScreenOrientation,
    viewModel: PlaylistDetailViewModel = koinViewModel(),
    onTrackSelected: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(playlistId) {
        viewModel.load(playlistId)
    }

    DetailScreenScaffold(
        title = uiState.playlist?.name ?: "Playlist",
        onBack = onBack,
        orientation = orientation
    ) { paddingValues ->
        DetailScreenBody(
            paddingValues = paddingValues,
            isLoading = uiState.isLoading && uiState.tracks.isEmpty() && uiState.playlist == null,
            error = uiState.error,
            hasContent = uiState.playlist != null || uiState.tracks.isNotEmpty(),
            onRetry = { viewModel.refresh() }
        ) {
            PlaylistDetailContent(
                playlist = uiState.playlist,
                tracks = uiState.tracks,
                isRefreshing = uiState.isLoading && uiState.tracks.isNotEmpty(),
                error = uiState.error,
                onRetry = { viewModel.refresh() },
                onTrackSelected = onTrackSelected
            )
        }
    }
}