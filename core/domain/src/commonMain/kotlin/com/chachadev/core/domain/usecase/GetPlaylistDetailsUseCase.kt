package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.Playlist
import com.chachadev.core.domain.repository.SpotifyRepository

class GetPlaylistDetailsUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(playlistId: String): Result<Playlist> {
        if (playlistId.isBlank()) {
            return Result.failure(IllegalArgumentException("Playlist ID cannot be empty"))
        }
        return repository.getPlaylist(playlistId)
    }
}


