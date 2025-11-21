package com.chachadev.spotifycmpclone.data.api

import com.chachadev.spotifycmpclone.data.dto.CurrentUsersPlaylistsDto
import com.chachadev.spotifycmpclone.data.dto.SpotifyUsersAlbumSavedDto
import com.chachadev.spotifycmpclone.data.dto.UserSavedEpisodesDto
import com.chachadev.spotifycmpclone.data.dto.UsersSavedShowsDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

class LibraryApiService(
    private val client: HttpClient,
    private val baseUrl: String = LibraryEndpoints.BASE_URL
) {
    /**
     * Get User's Saved Albums
     * GET /me/albums
     */
    suspend fun getUserSavedAlbums(
        accessToken: String
    ): SpotifyUsersAlbumSavedDto {
        return client.get("$baseUrl/me/albums") {
            header("Authorization", "Bearer $accessToken")
        }.body()
    }

    /**
     * Get Current User's Playlists
     * GET /me/playlists
     */
    suspend fun getCurrentUserPlaylists(
        accessToken: String
    ): CurrentUsersPlaylistsDto {
        return client.get("$baseUrl/me/playlists") {
            header("Authorization", "Bearer $accessToken")
        }.body()
    }

    /**
     * Get User's Saved Podcasts/Shows
     * GET /me/shows
     */
    suspend fun getUserSavedShows(
        accessToken: String
    ): UsersSavedShowsDto {
        return client.get("$baseUrl/me/shows") {
            header("Authorization", "Bearer $accessToken")
        }.body()
    }

    /**
     * Get User's Saved Episodes
     * GET /me/episodes
     */
    suspend fun getUserSavedEpisodes(
        accessToken: String
    ): UserSavedEpisodesDto {
        return client.get("$baseUrl/me/episodes") {
            header("Authorization", "Bearer $accessToken")
        }.body()
    }
}


object LibraryEndpoints {
    const val BASE_URL = "https://api.spotify.com/v1"

    // Get User's Saved Albums
    const val GET_USER_SAVED_ALBUMS = "$BASE_URL/me/albums"

    // Get Current User's Playlists
    const val GET_CURRENT_USER_PLAYLISTS = "$BASE_URL/me/playlists"

    // Get User's Saved Podcasts/Shows
    const val GET_USER_SAVED_SHOWS = "$BASE_URL/me/shows"

    // Get User's Saved Episodes
    const val GET_USER_SAVED_EPISODES = "$BASE_URL/me/episodes"
}
