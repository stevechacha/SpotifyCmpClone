package com.chachadev.core.data.repository

import com.chachadev.core.network.datasource.SpotifyRemoteDataSource
import com.chachadev.core.network.auth.AuthProvider
import com.chachadev.core.domain.model.CurrentUsersPlaylists
import com.chachadev.core.domain.model.SpotifyUsersAlbumSaved
import com.chachadev.core.domain.model.UserSavedEpisodes
import com.chachadev.core.domain.model.UsersSavedShows
import com.chachadev.core.domain.repository.LibraryRepository

class LibraryRepositoryImpl(
    private val remoteDataSource: SpotifyRemoteDataSource,
    private val authProvider: AuthProvider
) : LibraryRepository {

    override suspend fun getUserSavedAlbums(): Result<SpotifyUsersAlbumSaved> {
        return runWithToken { token ->
            remoteDataSource.getUserSavedAlbums(token).toDomain()
        }
    }

    override suspend fun getCurrentUserPlaylists(): Result<CurrentUsersPlaylists> {
        return runWithToken { token ->
            remoteDataSource.getCurrentUserPlaylists(token).toDomain()
        }
    }

    override suspend fun getUserSavedShows(): Result<UsersSavedShows> {
        return runWithToken { token ->
            remoteDataSource.getUserSavedShows(token).toDomain()
        }
    }

    override suspend fun getUserSavedEpisodes(): Result<UserSavedEpisodes> {
        return runWithToken { token ->
            remoteDataSource.getUserSavedEpisodes(token).toDomain()
        }
    }

    private suspend fun <T> runWithToken(block: suspend (String) -> T): Result<T> {
        return try {
            val token = authProvider.getValidToken()
            Result.success(block(token))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

