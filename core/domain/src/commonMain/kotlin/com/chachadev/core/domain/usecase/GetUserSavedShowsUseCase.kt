package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.UsersSavedShows
import com.chachadev.core.domain.repository.LibraryRepository

class GetUserSavedShowsUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(): Result<UsersSavedShows> {
        return repository.getUserSavedShows()
    }
}

