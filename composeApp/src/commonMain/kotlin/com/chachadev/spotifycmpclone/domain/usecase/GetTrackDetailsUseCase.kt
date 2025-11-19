package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.Track
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository

class GetTrackDetailsUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(trackId: String): Result<Track> {
        if (trackId.isBlank()) {
            return Result.failure(IllegalArgumentException("Track ID cannot be empty"))
        }
        return repository.getTrack(trackId)
    }
}


