package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.Artist
import com.chachadev.core.domain.repository.SpotifyRepository

class GetArtistDetailsUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(artistId: String): Result<Artist> {
        if (artistId.isBlank()) {
            return Result.failure(IllegalArgumentException("Artist ID cannot be empty"))
        }
        return repository.getArtist(artistId)
    }
}


