package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.Album
import com.chachadev.core.domain.repository.SpotifyRepository

class GetAlbumDetailsUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(albumId: String): Result<Album> {
        if (albumId.isBlank()) {
            return Result.failure(IllegalArgumentException("Album ID cannot be empty"))
        }
        return repository.getAlbum(albumId)
    }
}


