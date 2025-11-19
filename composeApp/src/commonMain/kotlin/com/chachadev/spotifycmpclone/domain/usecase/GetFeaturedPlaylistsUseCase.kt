package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.Playlist
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository

class GetFeaturedPlaylistsUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(
        limit: Int = 20,
        offset: Int = 0
    ): Result<List<Playlist>> {
        return repository.getFeaturedPlaylists(limit, offset)
    }
}

