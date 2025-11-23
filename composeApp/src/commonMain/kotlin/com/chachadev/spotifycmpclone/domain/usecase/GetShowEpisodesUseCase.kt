package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.Episode
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository

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

