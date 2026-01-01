package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.UserSavedEpisodes
import com.chachadev.core.domain.repository.LibraryRepository

class GetUserSavedEpisodesUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(): Result<UserSavedEpisodes> {
        return repository.getUserSavedEpisodes()
    }
}

