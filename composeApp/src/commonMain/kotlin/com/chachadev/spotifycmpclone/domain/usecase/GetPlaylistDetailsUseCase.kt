package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.Playlist
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository

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


