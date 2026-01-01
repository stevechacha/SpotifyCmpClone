package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.Playlist
import com.chachadev.core.domain.repository.SpotifyRepository

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

