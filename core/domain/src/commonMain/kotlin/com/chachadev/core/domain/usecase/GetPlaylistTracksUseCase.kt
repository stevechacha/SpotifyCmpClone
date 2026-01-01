package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.Track
import com.chachadev.core.domain.repository.SpotifyRepository

class GetPlaylistTracksUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(playlistId: String): Result<List<Track>> {
        if (playlistId.isBlank()) {
            return Result.failure(IllegalArgumentException("Playlist ID cannot be empty"))
        }
        return repository.getPlaylistTracks(playlistId)
    }
}


