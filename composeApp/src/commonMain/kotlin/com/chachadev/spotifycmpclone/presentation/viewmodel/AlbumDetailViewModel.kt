package com.chachadev.spotifycmpclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.core.domain.model.Album
import com.chachadev.core.domain.model.Track
import com.chachadev.core.domain.usecase.GetAlbumDetailsUseCase
import com.chachadev.core.domain.usecase.GetAlbumTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumDetailViewModel(
    private val getAlbumDetailsUseCase: GetAlbumDetailsUseCase,
    private val getAlbumTracksUseCase: GetAlbumTracksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumDetailUiState())
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    private var currentAlbumId: String? = null

    fun load(albumId: String) {
        if (albumId.isBlank()) return
        currentAlbumId = albumId
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val albumResult = getAlbumDetailsUseCase(albumId)
            val tracksResult = getAlbumTracksUseCase(albumId)

            val album = albumResult.getOrNull()
            val tracks = tracksResult.getOrNull()
                ?.map { track ->
                    if (track.album == null && album != null) {
                        track.copy(album = album)
                    } else {
                        track
                    }
                }.orEmpty()

            val error = albumResult.exceptionOrNull()?.message
                ?: tracksResult.exceptionOrNull()?.message

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                album = album,
                tracks = tracks,
                error = error
            )
        }
    }

    fun refresh() {
        currentAlbumId?.let { load(it) }
    }
}

data class AlbumDetailUiState(
    val isLoading: Boolean = false,
    val album: Album? = null,
    val tracks: List<Track> = emptyList(),
    val error: String? = null
)


