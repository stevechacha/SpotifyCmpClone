package com.chachadev.spotifycmpclone.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.chachadev.core.common.screen.ScreenOrientation
import com.chachadev.spotifycmpclone.presentation.viewmodel.AlbumDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AlbumScreen(
    albumId: String,
    orientation: ScreenOrientation,
    viewModel: AlbumDetailViewModel = koinViewModel(),
    onTrackSelected: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(albumId) {
        viewModel.load(albumId)
    }

    DetailScreenScaffold(
        title = uiState.album?.name ?: "Album",
        onBack = onBack,
        orientation = orientation
    ) { paddingValues ->
        DetailScreenBody(
            paddingValues = paddingValues,
            isLoading = uiState.isLoading && uiState.tracks.isEmpty() && uiState.album == null,
            error = uiState.error,
            hasContent = uiState.album != null || uiState.tracks.isNotEmpty(),
            onRetry = { viewModel.refresh() }
        ) {
            AlbumDetailContent(
                album = uiState.album,
                tracks = uiState.tracks,
                isRefreshing = uiState.isLoading && uiState.tracks.isNotEmpty(),
                error = uiState.error,
                onRetry = { viewModel.refresh() },
                onTrackSelected = onTrackSelected
            )
        }
    }
}