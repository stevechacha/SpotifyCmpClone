package com.chachadev.spotifycmpclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.spotifycmpclone.domain.model.Artist
import com.chachadev.spotifycmpclone.domain.model.Track
import com.chachadev.spotifycmpclone.domain.usecase.GetArtistDetailsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetArtistTopTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArtistDetailViewModel(
    private val getArtistDetailsUseCase: GetArtistDetailsUseCase,
    private val getArtistTopTracksUseCase: GetArtistTopTracksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistDetailUiState())
    val uiState: StateFlow<ArtistDetailUiState> = _uiState.asStateFlow()

    private var currentArtistId: String? = null

    fun load(artistId: String) {
        if (artistId.isBlank()) return
        currentArtistId = artistId
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val artistResult = getArtistDetailsUseCase(artistId)
            val tracksResult = getArtistTopTracksUseCase(artistId)

            val artist = artistResult.getOrNull()
            val tracks = tracksResult.getOrNull().orEmpty()
            val error = artistResult.exceptionOrNull()?.message
                ?: tracksResult.exceptionOrNull()?.message

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                artist = artist,
                topTracks = tracks,
                error = error
            )
        }
    }

    fun refresh() {
        currentArtistId?.let { load(it) }
    }
}

data class ArtistDetailUiState(
    val isLoading: Boolean = false,
    val artist: Artist? = null,
    val topTracks: List<Track> = emptyList(),
    val error: String? = null
)


