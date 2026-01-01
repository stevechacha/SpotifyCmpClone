package com.chachadev.spotifycmpclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.core.data.auth.AuthManager
import com.chachadev.core.domain.model.Album
import com.chachadev.core.domain.model.PlaylistItem
import com.chachadev.core.domain.usecase.GetCurrentUserPlaylistsUseCase
import com.chachadev.core.domain.usecase.GetUserSavedAlbumsUseCase
import com.chachadev.core.domain.usecase.GetUserSavedEpisodesUseCase
import com.chachadev.core.domain.usecase.GetUserSavedShowsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val getUserSavedAlbumsUseCase: GetUserSavedAlbumsUseCase,
    private val getCurrentUserPlaylistsUseCase: GetCurrentUserPlaylistsUseCase,
    private val getUserSavedShowsUseCase: GetUserSavedShowsUseCase,
    private val getUserSavedEpisodesUseCase: GetUserSavedEpisodesUseCase,
    private val authManager: AuthManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Combine auth state with library loading
            authManager.isSignedIn.collect { isSignedIn ->
                if (isSignedIn) {
                    loadLibraryContent()
                } else {
                    _uiState.value = LibraryUiState(
                        isLoading = false,
                        requiresSignIn = true
                    )
                }
            }
        }
    }

    fun loadLibraryContent() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null, requiresSignIn = false)
        viewModelScope.launch {
            val albumsResult = getUserSavedAlbumsUseCase()
            val playlistsResult = getCurrentUserPlaylistsUseCase()
            val showsResult = getUserSavedShowsUseCase()
            val episodesResult = getUserSavedEpisodesUseCase()

            val savedAlbums = albumsResult.getOrNull()?.items?.mapNotNull { it.album } ?: emptyList()
            val playlists = playlistsResult.getOrNull()?.items?.filterNotNull() ?: emptyList()
            val shows = showsResult.getOrNull()?.items?.mapNotNull { it.show } ?: emptyList()
            val episodes = episodesResult.getOrNull()?.items?.mapNotNull { it.episode } ?: emptyList()

            val error = albumsResult.exceptionOrNull()?.message
                ?: playlistsResult.exceptionOrNull()?.message
                ?: showsResult.exceptionOrNull()?.message
                ?: episodesResult.exceptionOrNull()?.message

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                savedAlbums = savedAlbums,
                playlists = playlists,
                savedShows = shows,
                savedEpisodes = episodes,
                error = error
            )
        }
    }

    fun refresh() {
        if (!_uiState.value.requiresSignIn) {
            loadLibraryContent()
        }
    }
}

data class LibraryUiState(
    val isLoading: Boolean = false,
    val savedAlbums: List<Album> = emptyList(),
    val playlists: List<PlaylistItem> = emptyList(),
    val savedShows: List<com.chachadev.core.domain.model.Show> = emptyList(),
    val savedEpisodes: List<com.chachadev.core.domain.model.Episode> = emptyList(),
    val error: String? = null,
    val requiresSignIn: Boolean = false
)

