package com.chachadev.spotifycmpclone.data.repository

import com.chachadev.spotifycmpclone.data.api.SpotifyApi
import com.chachadev.spotifycmpclone.data.auth.AuthManager
import com.chachadev.spotifycmpclone.domain.model.Album
import com.chachadev.spotifycmpclone.domain.model.Artist
import com.chachadev.spotifycmpclone.domain.model.Playlist
import com.chachadev.spotifycmpclone.domain.model.SearchResult
import com.chachadev.spotifycmpclone.domain.model.Track
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

    private suspend fun <T> runWithToken(block: suspend (String) -> T): Result<T> {
        return try {
            val token = authManager.getValidToken()
            Result.success(block(token))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

