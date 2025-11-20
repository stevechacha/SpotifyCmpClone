package com.chachadev.spotifycmpclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.spotifycmpclone.domain.model.Track
import com.chachadev.spotifycmpclone.domain.usecase.GetTrackDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrackDetailViewModel(
    private val getTrackDetailsUseCase: GetTrackDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackDetailUiState())
    val uiState: StateFlow<TrackDetailUiState> = _uiState.asStateFlow()

    private var currentTrackId: String? = null

    fun load(trackId: String) {
        if (trackId.isBlank()) return
        currentTrackId = trackId
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val result = getTrackDetailsUseCase(trackId)
            val track = result.getOrNull()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                track = track,
                error = result.exceptionOrNull()?.message
            )
        }
    }

    fun refresh() {
        currentTrackId?.let { load(it) }
    }
}

data class TrackDetailUiState(
    val isLoading: Boolean = false,
    val track: Track? = null,
    val error: String? = null
)





