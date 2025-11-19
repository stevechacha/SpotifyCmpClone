package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.Track
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository

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


