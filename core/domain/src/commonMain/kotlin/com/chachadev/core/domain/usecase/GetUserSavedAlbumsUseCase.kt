package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.SpotifyUsersAlbumSaved
import com.chachadev.core.domain.repository.LibraryRepository

class GetUserSavedAlbumsUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(): Result<SpotifyUsersAlbumSaved> {
        return repository.getUserSavedAlbums()
    }
}

