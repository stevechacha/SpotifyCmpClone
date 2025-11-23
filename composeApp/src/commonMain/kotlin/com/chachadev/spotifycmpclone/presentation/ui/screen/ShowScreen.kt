package com.chachadev.spotifycmpclone.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.chachadev.core.common.screen.ScreenOrientation
import com.chachadev.spotifycmpclone.presentation.viewmodel.ShowDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShowScreen(
    showId: String,
    orientation: ScreenOrientation,
    viewModel: ShowDetailViewModel = koinViewModel(),
    onEpisodeSelected: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(showId) {
        viewModel.load(showId)
    }

    DetailScreenScaffold(
        title = uiState.show?.name ?: "Podcast",
        onBack = onBack,
        orientation = orientation
    ) { paddingValues ->
        DetailScreenBody(
            paddingValues = paddingValues,
            isLoading = uiState.isLoading && uiState.episodes.isEmpty() && uiState.show == null,
            error = uiState.error,
            hasContent = uiState.show != null || uiState.episodes.isNotEmpty(),
            onRetry = { viewModel.refresh() }
        ) {
            ShowDetailContent(
                show = uiState.show,
                episodes = uiState.episodes,
                isRefreshing = uiState.isLoading && uiState.episodes.isNotEmpty(),
                error = uiState.error,
                onRetry = { viewModel.refresh() },
                onEpisodeSelected = onEpisodeSelected
            )
        }
    }
}

