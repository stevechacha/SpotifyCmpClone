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
    var onStartOAuthFlow: ((String) -> Unit)? = null

    init {
        // Check if user is already signed in
        viewModelScope.launch {
            authManager.isSignedIn.collect { isSignedIn ->
                if (isSignedIn && !_uiState.value.isLoggedIn) {
                    // User is signed in, fetch profile
                    loadUserProfile()
                } else if (!isSignedIn) {
                    _uiState.value = _uiState.value.copy(isLoggedIn = false, user = null, error = null)
                }
            }
        }
        
        // Also check on init if user is already signed in
        viewModelScope.launch {
            if (authManager.isSignedIn.value && !_uiState.value.isLoggedIn) {
                loadUserProfile()
            }
        }
    }

    fun login() {
        // Trigger OAuth flow by getting sign-in URL and opening it
        viewModelScope.launch {
            val signInUrl = authManager.getSignInUrl()
            onStartOAuthFlow?.invoke(signInUrl)
        }
    }
    
    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            // Check if still signed in before making API call
            if (!authManager.isSignedIn.value) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = false,
                    user = null,
                    error = "Session expired. Please sign in again."
                )
                return@launch
            }
            
            val userResult = repository.getCurrentUser()
            val user = userResult.getOrNull()
            val error = userResult.exceptionOrNull()?.message
            
            // If API call failed with auth error, sign out
            if (userResult.isFailure && error?.contains("401") == true || error?.contains("403") == true) {
                println("ProfileViewModel: Auth error detected - signing out")
                authManager.setSignedIn(false)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = false,
                    user = null,
                    error = "Session expired. Please sign in again."
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = authManager.isSignedIn.value,
                    user = user,
                    error = error
                )
            }
        }
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

