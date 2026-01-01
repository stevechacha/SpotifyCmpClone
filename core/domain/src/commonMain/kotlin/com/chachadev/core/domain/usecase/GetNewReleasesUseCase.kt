package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.Album
import com.chachadev.core.domain.repository.SpotifyRepository

class GetNewReleasesUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(
        limit: Int = 20,
        offset: Int = 0
    ): Result<List<Album>> {
        return repository.getNewReleases(limit, offset)
    }
}

