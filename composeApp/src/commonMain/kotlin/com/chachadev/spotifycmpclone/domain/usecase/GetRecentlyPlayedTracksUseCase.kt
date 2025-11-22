package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.Track
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository

class GetRecentlyPlayedTracksUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(
        limit: Int = 20
    ): Result<List<Track>> {
        return repository.getRecentlyPlayedTracks(limit)
    }
}

