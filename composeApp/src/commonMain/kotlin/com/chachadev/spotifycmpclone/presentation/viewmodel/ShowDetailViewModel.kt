package com.chachadev.spotifycmpclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.core.domain.model.Episode
import com.chachadev.core.domain.model.Show
import com.chachadev.core.domain.usecase.GetShowDetailsUseCase
import com.chachadev.core.domain.usecase.GetShowEpisodesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShowDetailViewModel(
    private val getShowDetailsUseCase: GetShowDetailsUseCase,
    private val getShowEpisodesUseCase: GetShowEpisodesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShowDetailUiState())
    val uiState: StateFlow<ShowDetailUiState> = _uiState.asStateFlow()

    private var currentShowId: String? = null

    fun load(showId: String) {
        if (showId.isBlank()) {
            println("ShowDetailViewModel: Show ID is blank, cannot load")
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Invalid show ID"
            )
            return
        }
        
        println("ShowDetailViewModel: Loading show with id=$showId")
        currentShowId = showId
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val showResult = getShowDetailsUseCase(showId)
                val episodesResult = getShowEpisodesUseCase(showId)

                val show = showResult.getOrNull()
                val episodes = episodesResult.getOrNull().orEmpty()
                val error = showResult.exceptionOrNull()?.message
                    ?: episodesResult.exceptionOrNull()?.message

                println("ShowDetailViewModel: Load complete - show=${show?.name}, episodes=${episodes.size}, error=$error")

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    show = show,
                    episodes = episodes,
                    error = error
                )
            } catch (e: Exception) {
                println("ShowDetailViewModel: Exception loading show: ${e.message}")
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load show"
                )
            }
        }
    }

    fun refresh() {
        currentShowId?.let { load(it) }
    }
}

data class ShowDetailUiState(
    val isLoading: Boolean = false,
    val show: Show? = null,
    val episodes: List<Episode> = emptyList(),
    val error: String? = null
)

