package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.UserSavedEpisodes
import com.chachadev.spotifycmpclone.domain.repository.LibraryRepository

class GetUserSavedEpisodesUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(): Result<UserSavedEpisodes> {
        return repository.getUserSavedEpisodes()
    }
}

