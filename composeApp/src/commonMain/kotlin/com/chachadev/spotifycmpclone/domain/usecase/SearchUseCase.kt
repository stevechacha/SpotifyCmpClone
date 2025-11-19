package com.chachadev.spotifycmpclone.domain.usecase

import com.chachadev.spotifycmpclone.domain.model.SearchResult
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository

class SearchUseCase(
    private val repository: SpotifyRepository
) {
    suspend operator fun invoke(
        query: String,
        type: String = "track,album,artist,playlist"
    ): Result<SearchResult> {
        if (query.isBlank()) {
            return Result.failure(IllegalArgumentException("Query cannot be empty"))
        }
        return repository.search(query, type)
    }
}

