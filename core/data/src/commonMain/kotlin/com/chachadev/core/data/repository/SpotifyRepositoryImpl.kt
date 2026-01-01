package com.chachadev.core.data.repository

import com.chachadev.core.network.datasource.SpotifyRemoteDataSource
import com.chachadev.core.network.auth.AuthProvider
import com.chachadev.core.domain.model.Album
import com.chachadev.core.domain.model.Artist
import com.chachadev.core.domain.model.Episode
import com.chachadev.core.domain.model.Playlist
import com.chachadev.core.domain.model.SearchResult
import com.chachadev.core.domain.model.Show
import com.chachadev.core.domain.model.Track
import com.chachadev.core.domain.model.User
import com.chachadev.core.domain.repository.SpotifyRepository

class SpotifyRepositoryImpl(
    private val remoteDataSource: SpotifyRemoteDataSource,
    private val authProvider: AuthProvider
) : SpotifyRepository {

    override suspend fun search(
        query: String,
        type: String
    ): Result<SearchResult> {
        return runWithToken { token ->
            val result = remoteDataSource.search(
                query = query,
                type = type,
                accessToken = token
            )
            result.toDomain()
        }
    }

    override suspend fun getTrack(id: String): Result<Track> {
        return runWithToken { token ->
            remoteDataSource.getTrack(id, token).toDomain()
        }
    }

    override suspend fun getAlbum(id: String): Result<Album> {
        return runWithToken { token ->
            remoteDataSource.getAlbum(id, token).toDomain()
        }
    }

    override suspend fun getArtist(id: String): Result<Artist> {
        return runWithToken { token ->
            remoteDataSource.getArtist(id, token).toDomain()
        }
    }

    override suspend fun getPlaylist(id: String): Result<Playlist> {
        return runWithToken { token ->
            remoteDataSource.getPlaylist(id, token).toDomain()
        }
    }

    override suspend fun getNewReleases(
        limit: Int,
        offset: Int
    ): Result<List<Album>> {
        return runWithToken { token ->
            val releases = remoteDataSource.getNewReleases(
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
            val response = remoteDataSource.getFeaturedPlaylists(
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
            val response = remoteDataSource.getArtistTopTracks(
                artistId = artistId,
                accessToken = token
            )
            response.tracks?.map { it.toDomain() } ?: emptyList()
        }
    }

    override suspend fun getAlbumTracks(albumId: String): Result<List<Track>> {
        return runWithToken { token ->
            val response = remoteDataSource.getAlbumTracks(
                albumId = albumId,
                accessToken = token
            )
            val trackDtos = response.tracks ?: response.items ?: emptyList()
            trackDtos.map { it.toDomain() }
        }
    }

    override suspend fun getPlaylistTracks(playlistId: String): Result<List<Track>> {
        return runWithToken { token ->
            val response = remoteDataSource.getPlaylistTracks(
                playlistId = playlistId,
                accessToken = token
            )
            response.items.mapNotNull { it.track.toDomain() }
        }
    }

    override suspend fun getShow(showId: String): Result<Show> {
        return runWithToken { token ->
            remoteDataSource.getShow(showId, token).toDomain()
        }
    }

    override suspend fun getShowEpisodes(showId: String, limit: Int, offset: Int): Result<List<Episode>> {
        return runWithToken { token ->
            val response = remoteDataSource.getShowEpisodes(
                showId = showId,
                accessToken = token,
                limit = limit,
                offset = offset
            )
            (response.items ?: emptyList()).map { it.toDomain() }
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            runWithToken { token ->
                try {
                    remoteDataSource.getCurrentUser(token).toDomain()
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
                
                val response = remoteDataSource.getRecentlyPlayedTracks(
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
                    val trackResult = runCatching { item.track?.toDomain() }
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
            val token = authProvider.getValidToken()
            Result.success(block(token))
        } catch (e: Exception) {
            println("SpotifyRepositoryImpl: Error getting valid token: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

