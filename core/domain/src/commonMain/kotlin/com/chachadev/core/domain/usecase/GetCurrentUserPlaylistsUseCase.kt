package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.CurrentUsersPlaylists
import com.chachadev.core.domain.repository.LibraryRepository

class GetCurrentUserPlaylistsUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(): Result<CurrentUsersPlaylists> {
        return repository.getCurrentUserPlaylists()
    }
}

