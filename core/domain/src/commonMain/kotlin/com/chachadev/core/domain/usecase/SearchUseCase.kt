package com.chachadev.core.domain.usecase

import com.chachadev.core.domain.model.SearchResult
import com.chachadev.core.domain.repository.SpotifyRepository

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

