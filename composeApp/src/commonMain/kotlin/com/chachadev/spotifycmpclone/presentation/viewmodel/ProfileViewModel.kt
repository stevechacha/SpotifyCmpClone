package com.chachadev.spotifycmpclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.spotifycmpclone.data.auth.AuthManager
import com.chachadev.spotifycmpclone.domain.model.User
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authManager: AuthManager,
    private val repository: SpotifyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    var onSignInSuccess: (() -> Unit)? = null

    fun login() {
        // OAuth flow will be handled by AuthScreen
        // This method is kept for backward compatibility but will trigger OAuth
        onSignInSuccess?.invoke()
    }
    
    suspend fun handleAuthCallback(code: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val tokenResult = authManager.exchangeCodeForToken(code)
                tokenResult.fold(
                    onSuccess = { tokenResponse ->
                        // Authentication successful, now try to fetch user profile
                        val userResult = repository.getCurrentUser()
                        val user = userResult.getOrNull()
                        val error = userResult.exceptionOrNull()?.message
                        
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            user = user,
                            error = error
                        )
                        
                        // Call success callback
                        onSignInSuccess?.invoke()
                    },
                    onFailure = { e ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = false,
                            error = e.message ?: "Failed to exchange authorization code"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = false,
                    error = e.message ?: "Failed to login"
                )
            }
        }
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

