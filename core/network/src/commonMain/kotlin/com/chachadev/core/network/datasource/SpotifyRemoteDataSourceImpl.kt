package com.chachadev.core.network.datasource

import com.chachadev.core.network.dto.spotify.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

class SpotifyRemoteDataSourceImpl(
    private val client: HttpClient,
    private val baseUrl: String = "https://api.spotify.com/v1"
) : SpotifyRemoteDataSource {

    override suspend fun search(
        query: String,
        type: String,
        limit: Int,
        offset: Int,
        accessToken: String
    ): SearchResultDto {
        return client.get("$baseUrl/search") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("q", query)
            parameter("type", type)
            parameter("limit", limit)
            parameter("offset", offset)
        }.body()
    }

    override suspend fun getTrack(id: String, accessToken: String): TrackDto {
        return client.get("$baseUrl/tracks/$id") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    override suspend fun getAlbum(id: String, accessToken: String): AlbumDto {
        return client.get("$baseUrl/albums/$id") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    override suspend fun getArtist(id: String, accessToken: String): ArtistDto {
        return client.get("$baseUrl/artists/$id") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    override suspend fun getSeveralArtist(ids: String, accessToken: String): ArtistsDto {
        return client.get("$baseUrl/artists") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("ids", ids)
        }.body()
    }

    override suspend fun getArtistsAlbums(id: String, accessToken: String): ArtistsAlbumDto {
        return client.get("$baseUrl/artists/$id/albums") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    override suspend fun getArtistTopTracks(artistId: String, accessToken: String): TracksResponseDto {
        return client.get("$baseUrl/artists/$artistId/top-tracks") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("market", "US")
        }.body()
    }

    override suspend fun getPlaylist(id: String, accessToken: String): PlaylistDto {
        return client.get("$baseUrl/playlists/$id") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    override suspend fun getNewReleases(
        limit: Int,
        offset: Int,
        accessToken: String
    ): NewReleasesDto {
        return client.get("$baseUrl/browse/new-releases") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("limit", limit)
            parameter("offset", offset)
        }.body()
    }

    override suspend fun getFeaturedPlaylists(
        limit: Int,
        offset: Int,
        accessToken: String
    ): PlaylistsResponseDto {
        return client.get("$baseUrl/browse/featured-playlists") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("limit", limit)
            parameter("offset", offset)
        }.body()
    }

    override suspend fun getAlbumTracks(albumId: String, accessToken: String): TracksResponseDto {
        return client.get("$baseUrl/albums/$albumId/tracks") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    override suspend fun getPlaylistTracks(playlistId: String, accessToken: String): PlaylistTracksResponseDto {
        return client.get("$baseUrl/playlists/$playlistId/tracks") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    override suspend fun getShow(showId: String, accessToken: String): ShowDto {
        return client.get("$baseUrl/shows/$showId") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    override suspend fun getShowEpisodes(
        showId: String,
        accessToken: String,
        limit: Int,
        offset: Int
    ): EpisodesResponseDto {
        return client.get("$baseUrl/shows/$showId/episodes") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("limit", limit)
            parameter("offset", offset)
        }.body()
    }

    override suspend fun getCurrentUser(accessToken: String): UserDto {
        return client.get("$baseUrl/me") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    override suspend fun getRecentlyPlayedTracks(
        limit: Int,
        accessToken: String,
        after: Long?,
        before: Long?
    ): RecentlyPlayedResponseDto {
        val httpResponse = client.get("$baseUrl/me/player/recently-played") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("limit", limit)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
        }
        
        if (!httpResponse.status.isSuccess()) {
            val errorBody = httpResponse.bodyAsText()
            throw Exception("Failed to fetch recently played tracks: ${httpResponse.status} - $errorBody")
        }
        
        return httpResponse.body()
    }

    override suspend fun getUserSavedAlbums(accessToken: String): SpotifyUsersAlbumSavedDto {
        return client.get("$baseUrl/me/albums") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    override suspend fun getCurrentUserPlaylists(accessToken: String): CurrentUsersPlaylistsDto {
        return client.get("$baseUrl/me/playlists") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    override suspend fun getUserSavedShows(accessToken: String): UsersSavedShowsDto {
        return client.get("$baseUrl/me/shows") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    override suspend fun getUserSavedEpisodes(accessToken: String): UserSavedEpisodesDto {
        return client.get("$baseUrl/me/episodes") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }
}


