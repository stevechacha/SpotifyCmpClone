package com.chachadev.spotifycmpclone.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.chachadev.spotifycmpclone.presentation.viewmodel.TrackDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrackScreen(
    trackId: String,
    viewModel: TrackDetailViewModel = koinViewModel(),
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(trackId) {
        viewModel.load(trackId)
    }

    DetailScreenScaffold(
        title = uiState.track?.name ?: "Track",
        onBack = onBack
    ) { paddingValues ->
        DetailScreenBody(
            paddingValues = paddingValues,
            isLoading = uiState.isLoading && uiState.track == null,
            error = uiState.error,
            hasContent = uiState.track != null,
            onRetry = { viewModel.refresh() }
        ) {
            TrackDetailContent(
                track = uiState.track,
                isRefreshing = uiState.isLoading && uiState.track != null,
                error = uiState.error,
                onRetry = { viewModel.refresh() }
            )
        }
    }
}