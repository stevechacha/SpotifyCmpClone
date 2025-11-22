package com.chachadev.spotifycmpclone.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Shared state manager for Home screen that persists across ViewModel instances.
 * Uses Flow-based state management to prevent race conditions and state resets.
 * All ViewModel instances share the same state Flow.
 */
object HomeStateManager {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    // Use a shared coroutine scope for loading operations
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    
    // Track if a load is in progress using the state itself
    private val isLoading: Boolean
        get() = _uiState.value.isLoading
    
    /**
     * Thread-safe state update using Flow's update function
     */
    fun updateState(update: (HomeUiState) -> HomeUiState) {
        _uiState.update(update)
    }
    
    /**
     * Check if we should load content. Returns true if:
     * - Not currently loading
     * - At least one section is empty (we need to load data)
     */
    fun shouldLoad(): Boolean {
        val current = _uiState.value
        return !current.isLoading && 
               (current.recentlyPlayedTracks.isEmpty() || 
                current.newReleases.isEmpty() || 
                current.featuredPlaylists.isEmpty())
    }
    
    /**
     * Atomically set loading state if not already loading.
     * Returns true if loading state was successfully set, false if already loading.
     */
    fun trySetLoading(): Boolean {
        var success = false
        _uiState.update { current ->
            if (current.isLoading) {
                // Already loading, don't change state
                current
            } else {
                // Set loading state
                success = true
                current.copy(isLoading = true, error = null)
            }
        }
        return success
    }
    
    /**
     * Get current state snapshot
     */
    fun getCurrentState(): HomeUiState = _uiState.value
    
    /**
     * Get the shared coroutine scope for loading operations
     */
    fun getScope(): CoroutineScope = managerScope
}

