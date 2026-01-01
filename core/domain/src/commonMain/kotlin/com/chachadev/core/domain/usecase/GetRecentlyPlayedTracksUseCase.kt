package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.Track
import com.chachadev.core.domain.repository.SpotifyRepository

class GetRecentlyPlayedTracksUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(
        limit: Int = 20
    ): Result<List<Track>> {
        return repository.getRecentlyPlayedTracks(limit)
    }
}

