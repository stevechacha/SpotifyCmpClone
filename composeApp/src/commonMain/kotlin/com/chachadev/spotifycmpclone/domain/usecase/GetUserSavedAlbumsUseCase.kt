package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.SpotifyUsersAlbumSaved
import com.chachadev.spotifycmpclone.domain.repository.LibraryRepository

class GetUserSavedAlbumsUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(): Result<SpotifyUsersAlbumSaved> {
        return repository.getUserSavedAlbums()
    }
}

