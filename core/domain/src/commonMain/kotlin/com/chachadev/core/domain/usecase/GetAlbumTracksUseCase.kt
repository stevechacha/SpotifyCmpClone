package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.Track
import com.chachadev.core.domain.repository.SpotifyRepository

class GetAlbumTracksUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(albumId: String): Result<List<Track>> {
        if (albumId.isBlank()) {
            return Result.failure(IllegalArgumentException("Album ID cannot be empty"))
        }
        return repository.getAlbumTracks(albumId)
    }
}


