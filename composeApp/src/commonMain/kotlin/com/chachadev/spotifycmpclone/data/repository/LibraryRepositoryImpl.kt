package com.chachadev.spotifycmpclone.data.repository

import com.chachadev.spotifycmpclone.data.api.LibraryApiService
import com.chachadev.spotifycmpclone.data.auth.AuthManager
import com.chachadev.spotifycmpclone.domain.model.CurrentUsersPlaylists
import com.chachadev.spotifycmpclone.domain.model.SpotifyUsersAlbumSaved
import com.chachadev.spotifycmpclone.domain.model.UserSavedEpisodes
import com.chachadev.spotifycmpclone.domain.model.UsersSavedShows
import com.chachadev.spotifycmpclone.domain.repository.LibraryRepository

class LibraryRepositoryImpl(
    private val libraryApiService: LibraryApiService,
    private val authManager: AuthManager
) : LibraryRepository {

    override suspend fun getUserSavedAlbums(): Result<SpotifyUsersAlbumSaved> {
        return runWithToken { token ->
            libraryApiService.getUserSavedAlbums(token).toDomain()
        }
    }

    override suspend fun getCurrentUserPlaylists(): Result<CurrentUsersPlaylists> {
        return runWithToken { token ->
            libraryApiService.getCurrentUserPlaylists(token).toDomain()
        }
    }

    override suspend fun getUserSavedShows(): Result<UsersSavedShows> {
        return runWithToken { token ->
            libraryApiService.getUserSavedShows(token).toDomain()
        }
    }

    override suspend fun getUserSavedEpisodes(): Result<UserSavedEpisodes> {
        return runWithToken { token ->
            libraryApiService.getUserSavedEpisodes(token).toDomain()
        }
    }

    private suspend fun <T> runWithToken(block: suspend (String) -> T): Result<T> {
        return try {
            val token = authManager.getValidToken()
            Result.success(block(token))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

