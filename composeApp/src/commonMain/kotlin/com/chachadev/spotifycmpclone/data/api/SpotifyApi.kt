package com.chachadev.spotifycmpclone.data.api

import com.chachadev.spotifycmpclone.data.dto.AlbumDto
import com.chachadev.spotifycmpclone.data.dto.ArtistDto
import com.chachadev.spotifycmpclone.data.dto.NewReleasesDto
import com.chachadev.spotifycmpclone.data.dto.PlaylistDto
import com.chachadev.spotifycmpclone.data.dto.RecentlyPlayedResponseDto
import com.chachadev.spotifycmpclone.data.dto.SearchResultDto
import com.chachadev.spotifycmpclone.data.dto.TrackDto
import com.chachadev.spotifycmpclone.data.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

class SpotifyApi(
    private val client: HttpClient,
    private val baseUrl: String = "https://api.spotify.com/v1"
) {
    suspend fun search(
        query: String,
        type: String,
        limit: Int = 20,
        offset: Int = 0,
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

    suspend fun getTrack(
        id: String,
        accessToken: String
    ): TrackDto {
        return client.get("$baseUrl/tracks/$id") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    suspend fun getAlbum(
        id: String,
        accessToken: String
    ): AlbumDto {
        return client.get("$baseUrl/albums/$id") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    suspend fun getArtist(
        id: String,
        accessToken: String
    ): ArtistDto {
        return client.get("$baseUrl/artists/$id") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    suspend fun getPlaylist(
        id: String,
        accessToken: String
    ): PlaylistDto {
        return client.get("$baseUrl/playlists/$id") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    suspend fun getNewReleases(
        limit: Int = 20,
        offset: Int = 0,
        accessToken: String
    ): NewReleasesDto {
        return client.get("$baseUrl/browse/new-releases") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("limit", limit)
            parameter("offset", offset)
        }.body()
    }

    suspend fun getFeaturedPlaylists(
        limit: Int = 20,
        offset: Int = 0,
        accessToken: String
    ): PlaylistsResponseDto {
        return client.get("$baseUrl/browse/featured-playlists") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("limit", limit)
            parameter("offset", offset)
        }.body()
    }

    suspend fun getArtistTopTracks(
        artistId: String,
        accessToken: String
    ): TracksResponseDto {
        return client.get("$baseUrl/artists/$artistId/top-tracks") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("market", "US")
        }.body()
    }

    suspend fun getAlbumTracks(
        albumId: String,
        accessToken: String
    ): TracksResponseDto {
        return client.get("$baseUrl/albums/$albumId/tracks") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    suspend fun getPlaylistTracks(
        playlistId: String,
        accessToken: String
    ): PlaylistTracksResponseDto {
        return client.get("$baseUrl/playlists/$playlistId/tracks") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    suspend fun getCurrentUser(
        accessToken: String
    ): UserDto {
        return client.get("$baseUrl/me") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    suspend fun getUserSavedAlbum(
        accessToken: String
    ): TracksResponseDto {
        return client.get("$baseUrl/me/albums")
            .body()
    }

    suspend fun getUserSavedTracks(
        accessToken: String
    ): TracksResponseDto {
        return client.get("$baseUrl/me/tracks") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    suspend fun getRecentlyPlayedTracks(
        limit: Int = 20,
        accessToken: String,
        after: Long? = null,
        before: Long? = null
    ): RecentlyPlayedResponseDto {
        val httpResponse = client.get("$baseUrl/me/player/recently-played") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("limit", limit)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
        }
        
        println("SpotifyApi: Recently played HTTP status: ${httpResponse.status}")
        
        if (!httpResponse.status.isSuccess()) {
            val errorBody = httpResponse.bodyAsText()
            println("SpotifyApi: Error response body: $errorBody")
            throw Exception("Failed to fetch recently played tracks: ${httpResponse.status} - $errorBody")
        }
        
        val responseBody = httpResponse.bodyAsText()
        println("SpotifyApi: Recently played response body: $responseBody")
        
        return httpResponse.body()
    }
}

@kotlinx.serialization.Serializable
data class PlaylistsResponseDto(
    val message: String? = null,
    val playlists: PlaylistsPagingDto? = null
)

@kotlinx.serialization.Serializable
data class PlaylistsPagingDto(
    val items: List<PlaylistDto>
)

@kotlinx.serialization.Serializable
data class TracksResponseDto(
    val tracks: List<TrackDto>? = null,
    val items: List<TrackDto>? = null
)

@kotlinx.serialization.Serializable
data class PlaylistTrackItemDto(
    val track: TrackDto
)

@kotlinx.serialization.Serializable
data class PlaylistTracksResponseDto(
    val items: List<PlaylistTrackItemDto>
)