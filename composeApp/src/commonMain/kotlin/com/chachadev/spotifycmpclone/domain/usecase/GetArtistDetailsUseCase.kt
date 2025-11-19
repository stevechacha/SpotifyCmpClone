package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.Artist
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository

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


