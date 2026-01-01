package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.Track
import com.chachadev.core.domain.repository.SpotifyRepository

class GetArtistTopTracksUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(artistId: String): Result<List<Track>> {
        if (artistId.isBlank()) {
            return Result.failure(IllegalArgumentException("Artist ID cannot be empty"))
        }
        return repository.getArtistTopTracks(artistId)
    }
}

