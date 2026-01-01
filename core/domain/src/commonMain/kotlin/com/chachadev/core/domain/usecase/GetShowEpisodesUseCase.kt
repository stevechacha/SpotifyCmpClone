package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.Episode
import com.chachadev.core.domain.repository.SpotifyRepository

class GetShowEpisodesUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(showId: String, limit: Int = 50, offset: Int = 0): Result<List<Episode>> {
        if (showId.isBlank()) {
            return Result.failure(IllegalArgumentException("Show ID cannot be empty"))
        }
        return repository.getShowEpisodes(showId, limit, offset)
    }
}

