package com.chachadev.spotifycmpclone.domain.repository

import com.chachadev.spotifycmpclone.domain.model.CurrentUsersPlaylists
import com.chachadev.spotifycmpclone.domain.model.SpotifyUsersAlbumSaved
import com.chachadev.spotifycmpclone.domain.model.UserSavedEpisodes
import com.chachadev.spotifycmpclone.domain.model.UsersSavedShows

interface LibraryRepository {
    suspend fun getUserSavedAlbums(): Result<SpotifyUsersAlbumSaved>
    suspend fun getCurrentUserPlaylists(): Result<CurrentUsersPlaylists>
    suspend fun getUserSavedShows(): Result<UsersSavedShows>
    suspend fun getUserSavedEpisodes(): Result<UserSavedEpisodes>
}

