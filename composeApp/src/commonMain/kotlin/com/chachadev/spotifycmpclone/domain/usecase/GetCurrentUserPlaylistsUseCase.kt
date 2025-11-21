package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.CurrentUsersPlaylists
import com.chachadev.spotifycmpclone.domain.repository.LibraryRepository

class GetCurrentUserPlaylistsUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(): Result<CurrentUsersPlaylists> {
        return repository.getCurrentUserPlaylists()
    }
}

