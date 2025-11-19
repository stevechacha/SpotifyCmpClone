package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.Album
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository

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


