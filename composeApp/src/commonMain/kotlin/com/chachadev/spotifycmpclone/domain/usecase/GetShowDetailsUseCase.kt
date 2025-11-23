package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.Show
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository

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

