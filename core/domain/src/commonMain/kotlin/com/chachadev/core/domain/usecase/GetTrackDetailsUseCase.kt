package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.Track
import com.chachadev.core.domain.repository.SpotifyRepository

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





