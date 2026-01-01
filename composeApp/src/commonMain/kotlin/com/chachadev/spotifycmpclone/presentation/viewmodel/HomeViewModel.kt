package com.chachadev.spotifycmpclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.core.domain.model.Album
import com.chachadev.core.domain.model.Playlist
import com.chachadev.core.domain.model.Track
import com.chachadev.core.domain.usecase.GetFeaturedPlaylistsUseCase
import com.chachadev.core.domain.usecase.GetNewReleasesUseCase
import com.chachadev.core.domain.usecase.GetRecentlyPlayedTracksUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getNewReleasesUseCase: GetNewReleasesUseCase,
    private val getFeaturedPlaylistsUseCase: GetFeaturedPlaylistsUseCase,
    private val getRecentlyPlayedTracksUseCase: GetRecentlyPlayedTracksUseCase,
) : ViewModel(){
    // Use shared state manager instead of instance state
    val uiState: StateFlow<HomeUiState> = HomeStateManager.uiState

    init {
        // Use Flow-based check to determine if we should load
        // This prevents race conditions by checking the actual state Flow
        if (HomeStateManager.shouldLoad()) {
            println("HomeViewModel: Initializing - loading content")
            loadHomeContent()
        } else {
            val currentState = HomeStateManager.getCurrentState()
            println("HomeViewModel: Initializing - already has data (recentlyPlayed=${currentState.recentlyPlayedTracks.size}, newReleases=${currentState.newReleases.size}, isLoading=${currentState.isLoading}), skipping load")
        }
    }

    fun loadHomeContent() {
        // Use viewModelScope to ensure the coroutine is cancelled when ViewModel is cleared
        // But still use the atomic loading check to prevent concurrent loads
        viewModelScope.launch {
            // Atomically set loading state - this prevents concurrent loads
            if (!HomeStateManager.trySetLoading()) {
                println("HomeViewModel: Already loading, skipping duplicate load")
                return@launch
            }
            
            try {
                val currentState = HomeStateManager.getCurrentState()
                println("HomeViewModel: Starting loadHomeContent - current state: recentlyPlayed=${currentState.recentlyPlayedTracks.size}, newReleases=${currentState.newReleases.size}, isLoading=${currentState.isLoading}")
                
                val newReleasesResult = getNewReleasesUseCase()
                val playlistsResult = getFeaturedPlaylistsUseCase()
                val recentlyPlayedResult = getRecentlyPlayedTracksUseCase(limit = 20)

                // Check which results succeeded
                val newReleasesSuccess = newReleasesResult.isSuccess
                val playlistsSuccess = playlistsResult.isSuccess
                val recentlyPlayedSuccess = recentlyPlayedResult.isSuccess
                
                // Get the latest state (in case it was updated by another operation)
                val latestState = HomeStateManager.getCurrentState()
                
                // Only update sections that succeeded - preserve existing data on error
                val newReleases = newReleasesResult.getOrNull() ?: latestState.newReleases
                val playlists = playlistsResult.getOrNull() ?: latestState.featuredPlaylists
                val recentlyPlayed = recentlyPlayedResult.getOrNull() ?: latestState.recentlyPlayedTracks
                
                // Log errors for debugging
                newReleasesResult.exceptionOrNull()?.let { 
                    println("HomeViewModel: Error loading new releases: ${it.message}")
                    it.printStackTrace()
                }
                playlistsResult.exceptionOrNull()?.let { 
                    println("HomeViewModel: Error loading playlists: ${it.message}")
                    it.printStackTrace()
                }
                recentlyPlayedResult.exceptionOrNull()?.let { 
                    println("HomeViewModel: Error loading recently played: ${it.message}")
                    println("HomeViewModel: Recently played exception: ${it}")
                    it.printStackTrace()
                }
                
                // Only set error if ALL sections failed
                // Don't show error if at least one section succeeded
                val error = if (!newReleasesSuccess && !playlistsSuccess && !recentlyPlayedSuccess) {
                    // All failed - show the first error
                    newReleasesResult.exceptionOrNull()?.message
                        ?: playlistsResult.exceptionOrNull()?.message
                        ?: recentlyPlayedResult.exceptionOrNull()?.message
                } else {
                    // At least one succeeded - only log errors, don't show them
                    null
                }

                println("HomeViewModel: State update - recentlyPlayed: ${recentlyPlayed.size}, newReleases: ${newReleases.size}, playlists: ${playlists.size}")
                
                // Update shared state atomically using Flow's update
                HomeStateManager.updateState {
                    it.copy(
                        isLoading = false,
                        newReleases = newReleases,
                        featuredPlaylists = playlists,
                        recentlyPlayedTracks = recentlyPlayed,
                        error = error
                    )
                }
                
                println("HomeViewModel: State after update - recentlyPlayed: ${HomeStateManager.getCurrentState().recentlyPlayedTracks.size}")
            } catch (e: Exception) {
                println("HomeViewModel: Unhandled exception in loadHomeContent: ${e.message}")
                e.printStackTrace()
                // Preserve existing data on exception - only update loading and error
                HomeStateManager.updateState { current ->
                    current.copy(
                        isLoading = false,
                        error = "Failed to load content: ${e.message ?: "Unknown error"}"
                    )
                }
            } catch (e: Throwable) {
                println("HomeViewModel: Unhandled throwable in loadHomeContent: ${e.message}")
                e.printStackTrace()
                // Preserve existing data on throwable - only update loading and error
                HomeStateManager.updateState { current ->
                    current.copy(
                        isLoading = false,
                        error = "Failed to load content: ${e.message ?: "Unknown error"}"
                    )
                }
            }
        }
    }

    fun refresh() {
        // Force refresh - loadHomeContent will handle the loading state
        loadHomeContent()
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val newReleases: List<Album> = emptyList(),
    val featuredPlaylists: List<Playlist> = emptyList(),
    val recentlyPlayedTracks: List<Track> = emptyList(),
    val error: String? = null
)

