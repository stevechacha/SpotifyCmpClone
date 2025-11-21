package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.UsersSavedShows
import com.chachadev.spotifycmpclone.domain.repository.LibraryRepository

class GetUserSavedShowsUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(): Result<UsersSavedShows> {
        return repository.getUserSavedShows()
    }
}

