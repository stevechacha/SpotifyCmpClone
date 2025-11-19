package com.chachadev.spotifycmpclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.spotifycmpclone.domain.model.Album
import com.chachadev.spotifycmpclone.domain.model.Playlist
import com.chachadev.spotifycmpclone.domain.usecase.GetFeaturedPlaylistsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetNewReleasesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getNewReleasesUseCase: GetNewReleasesUseCase,
    private val getFeaturedPlaylistsUseCase: GetFeaturedPlaylistsUseCase,
) : ViewModel(){
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeContent()
    }

    fun loadHomeContent() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val newReleasesResult = getNewReleasesUseCase()
            val playlistsResult = getFeaturedPlaylistsUseCase()

            val newReleases = newReleasesResult.getOrNull() ?: emptyList()
            val playlists = playlistsResult.getOrNull() ?: emptyList()
            val error = newReleasesResult.exceptionOrNull()?.message
                ?: playlistsResult.exceptionOrNull()?.message

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                newReleases = newReleases,
                featuredPlaylists = playlists,
                error = error
            )
        }
    }

    fun refresh() {
        loadHomeContent()
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val newReleases: List<Album> = emptyList(),
    val featuredPlaylists: List<Playlist> = emptyList(),
    val error: String? = null
)

