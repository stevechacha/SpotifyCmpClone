package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.Show
import com.chachadev.core.domain.repository.SpotifyRepository

class GetShowDetailsUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(showId: String): Result<Show> {
        if (showId.isBlank()) {
            return Result.failure(IllegalArgumentException("Show ID cannot be empty"))
        }
        return repository.getShow(showId)
    }
}

