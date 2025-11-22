package com.chachadev.spotifycmpclone.data.repository

import com.chachadev.spotifycmpclone.data.api.SpotifyApi
import com.chachadev.spotifycmpclone.data.auth.AuthManager
import com.chachadev.spotifycmpclone.domain.model.Album
import com.chachadev.spotifycmpclone.domain.model.Artist
import com.chachadev.spotifycmpclone.domain.model.Playlist
import com.chachadev.spotifycmpclone.domain.model.SearchResult
import com.chachadev.spotifycmpclone.domain.model.Track
import com.chachadev.spotifycmpclone.domain.model.User
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository

class SpotifyRepositoryImpl(
    private val api: SpotifyApi,
    private val authManager: AuthManager
) : SpotifyRepository {

    override suspend fun search(
        query: String,
        type: String
    ): Result<SearchResult> {
        return runWithToken { token ->
            val result = api.search(
                query = query,
                type = type,
                accessToken = token
            )
            result.toDomain()
        }
    }

    override suspend fun getTrack(id: String): Result<Track> {
        return runWithToken { token ->
            api.getTrack(id, token).toDomain()
        }
    }

    override suspend fun getAlbum(id: String): Result<Album> {
        return runWithToken { token ->
            api.getAlbum(id, token).toDomain()
        }
    }

    override suspend fun getArtist(id: String): Result<Artist> {
        return runWithToken { token ->
            api.getArtist(id, token).toDomain()
        }
    }

    override suspend fun getPlaylist(id: String): Result<Playlist> {
        return runWithToken { token ->
            api.getPlaylist(id, token).toDomain()
        }
    }

    override suspend fun getNewReleases(
        limit: Int,
        offset: Int
    ): Result<List<Album>> {
        return runWithToken { token ->
            val releases = api.getNewReleases(
                limit = limit,
                offset = offset,
                accessToken = token
            )
            releases.toDomain()
        }
    }

    override suspend fun getFeaturedPlaylists(
        limit: Int,
        offset: Int
    ): Result<List<Playlist>> {
        return runWithToken { token ->
            val response = api.getFeaturedPlaylists(
                limit = limit,
                offset = offset,
                accessToken = token
            )
            response.playlists?.items
                ?.mapNotNull { dto ->
                    runCatching { dto.toDomain() }.getOrNull()
                }
                ?: emptyList()
        }
    }

    override suspend fun getArtistTopTracks(artistId: String): Result<List<Track>> {
        return runWithToken { token ->
            val response = api.getArtistTopTracks(
                artistId = artistId,
                accessToken = token
            )
            response.tracks?.map { it.toDomain() } ?: emptyList()
        }
    }

    override suspend fun getAlbumTracks(albumId: String): Result<List<Track>> {
        return runWithToken { token ->
            val response = api.getAlbumTracks(
                albumId = albumId,
                accessToken = token
            )
            val trackDtos = response.tracks ?: response.items ?: emptyList()
            trackDtos.map { it.toDomain() }
        }
    }

    override suspend fun getPlaylistTracks(playlistId: String): Result<List<Track>> {
        return runWithToken { token ->
            val response = api.getPlaylistTracks(
                playlistId = playlistId,
                accessToken = token
            )
            response.items.mapNotNull { it.track.toDomain() }
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            runWithToken { token ->
                try {
                    api.getCurrentUser(token).toDomain()
                } catch (e: Exception) {
                    // Handle serialization errors or API errors
                    throw Exception("User profile unavailable. The current authentication method (client credentials) doesn't support user profile access. Please use OAuth 2.0 Authorization Code flow to access user profile.")
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecentlyPlayedTracks(limit: Int): Result<List<Track>> {
        return runWithToken { token ->
            try {
                println("SpotifyRepositoryImpl: Fetching recently played tracks with limit=$limit")
                println("SpotifyRepositoryImpl: Token length: ${token.length}")
                
                val response = api.getRecentlyPlayedTracks(
                    limit = limit,
                    accessToken = token
                )
                println("SpotifyRepositoryImpl: API response received")
                println("SpotifyRepositoryImpl: Response href: ${response.href}")
                println("SpotifyRepositoryImpl: Response limit: ${response.limit}")
                println("SpotifyRepositoryImpl: Response next: ${response.next}")
                println("SpotifyRepositoryImpl: Response total: ${response.total}")
                println("SpotifyRepositoryImpl: Response cursors: ${response.cursors}")
                println("SpotifyRepositoryImpl: Response items: ${response.items?.size ?: "null"}")
                
                val itemsList = response.items ?: emptyList()
                println("SpotifyRepositoryImpl: Items list size: ${itemsList.size}")
                
                if (itemsList.isEmpty()) {
                    println("SpotifyRepositoryImpl: WARNING - API returned empty items list")
                    println("SpotifyRepositoryImpl: This could mean:")
                    println("  - No tracks have been played recently")
                    println("  - Token doesn't have 'user-read-recently-played' scope")
                    println("  - Tracks were played on a different device/account")
                }
                
                // Sort by playedAt timestamp (most recent first)
                // Spotify API returns ISO 8601 format: "2024-01-15T10:30:00Z"
                // ISO 8601 strings are lexicographically sortable, so we can use string comparison
                val sortedItems = itemsList.sortedByDescending { item ->
                    item.playedAt ?: "" // Empty string will sort to the end
                }
                
                val tracks = sortedItems.mapNotNull { item ->
                    if (item.track == null) {
                        println("SpotifyRepositoryImpl: Item has null track, skipping")
                        return@mapNotNull null
                    }
                    val trackResult = runCatching { item.track.toDomain() }
                    trackResult.onFailure { error ->
                        println("SpotifyRepositoryImpl: Failed to convert track: ${error.message}")
                        error.printStackTrace()
                    }
                    trackResult.getOrNull()
                }
                println("SpotifyRepositoryImpl: Successfully converted ${tracks.size} tracks out of ${itemsList.size} items (sorted by most recent)")
                tracks
            } catch (e: Exception) {
                println("SpotifyRepositoryImpl: Error fetching recently played tracks: ${e.message}")
                println("SpotifyRepositoryImpl: Exception type: ${e::class.simpleName}")
                e.printStackTrace()
                throw e
            }
        }
    }

    private suspend fun <T> runWithToken(block: suspend (String) -> T): Result<T> {
        return try {
            val token = authManager.getValidToken()
            Result.success(block(token))
        } catch (e: Exception) {
            println("SpotifyRepositoryImpl: Error getting valid token: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

