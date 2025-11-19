package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.Track
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository

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

