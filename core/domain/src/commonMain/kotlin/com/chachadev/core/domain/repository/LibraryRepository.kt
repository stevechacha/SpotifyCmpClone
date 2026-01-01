package com.chachadev.core.domain.repository

import com.chachadev.core.domain.model.CurrentUsersPlaylists
import com.chachadev.core.domain.model.SpotifyUsersAlbumSaved
import com.chachadev.core.domain.model.UserSavedEpisodes
import com.chachadev.core.domain.model.UsersSavedShows

interface LibraryRepository {
    suspend fun getUserSavedAlbums(): Result<SpotifyUsersAlbumSaved>
    suspend fun getCurrentUserPlaylists(): Result<CurrentUsersPlaylists>
    suspend fun getUserSavedShows(): Result<UsersSavedShows>
    suspend fun getUserSavedEpisodes(): Result<UserSavedEpisodes>
}

