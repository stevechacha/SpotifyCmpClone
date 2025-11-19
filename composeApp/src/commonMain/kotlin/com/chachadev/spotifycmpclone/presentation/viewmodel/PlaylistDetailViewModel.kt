package com.chachadev.spotifycmpclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.spotifycmpclone.domain.model.Playlist
import com.chachadev.spotifycmpclone.domain.model.Track
import com.chachadev.spotifycmpclone.domain.usecase.GetPlaylistDetailsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetPlaylistTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private val getPlaylistDetailsUseCase: GetPlaylistDetailsUseCase,
    private val getPlaylistTracksUseCase: GetPlaylistTracksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaylistDetailUiState())
    val uiState: StateFlow<PlaylistDetailUiState> = _uiState.asStateFlow()

    private var currentPlaylistId: String? = null

    fun load(playlistId: String) {
        if (playlistId.isBlank()) return
        currentPlaylistId = playlistId
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val playlistResult = getPlaylistDetailsUseCase(playlistId)
            val tracksResult = getPlaylistTracksUseCase(playlistId)

            val playlist = playlistResult.getOrNull()
            val tracks = tracksResult.getOrNull().orEmpty()
            val error = playlistResult.exceptionOrNull()?.message
                ?: tracksResult.exceptionOrNull()?.message

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                playlist = playlist,
                tracks = tracks,
                error = error
            )
        }
    }

    fun refresh() {
        currentPlaylistId?.let { load(it) }
    }
}

data class PlaylistDetailUiState(
    val isLoading: Boolean = false,
    val playlist: Playlist? = null,
    val tracks: List<Track> = emptyList(),
    val error: String? = null
)


