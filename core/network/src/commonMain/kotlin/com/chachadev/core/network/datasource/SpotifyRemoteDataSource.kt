package com.chachadev.core.network.datasource

import com.chachadev.core.network.dto.spotify.*

interface SpotifyRemoteDataSource {
    suspend fun search(
        query: String,
        type: String,
        limit: Int = 20,
        offset: Int = 0,
        accessToken: String
    ): SearchResultDto

    suspend fun getTrack(id: String, accessToken: String): TrackDto
    suspend fun getAlbum(id: String, accessToken: String): AlbumDto
    suspend fun getArtist(id: String, accessToken: String): ArtistDto
    suspend fun getSeveralArtist(ids: String, accessToken: String): ArtistsDto
    suspend fun getArtistsAlbums(id: String, accessToken: String): ArtistsAlbumDto
    suspend fun getArtistTopTracks(artistId: String, accessToken: String): TracksResponseDto
    suspend fun getPlaylist(id: String, accessToken: String): PlaylistDto
    suspend fun getNewReleases(
        limit: Int = 20,
        offset: Int = 0,
        accessToken: String
    ): NewReleasesDto
    suspend fun getFeaturedPlaylists(
        limit: Int = 20,
        offset: Int = 0,
        accessToken: String
    ): PlaylistsResponseDto
    suspend fun getAlbumTracks(albumId: String, accessToken: String): TracksResponseDto
    suspend fun getPlaylistTracks(playlistId: String, accessToken: String): PlaylistTracksResponseDto
    suspend fun getShow(showId: String, accessToken: String): ShowDto
    suspend fun getShowEpisodes(
        showId: String,
        accessToken: String,
        limit: Int = 50,
        offset: Int = 0
    ): EpisodesResponseDto
    suspend fun getCurrentUser(accessToken: String): UserDto
    suspend fun getRecentlyPlayedTracks(
        limit: Int = 20,
        accessToken: String,
        after: Long? = null,
        before: Long? = null
    ): RecentlyPlayedResponseDto
    
    // Library endpoints
    suspend fun getUserSavedAlbums(accessToken: String): SpotifyUsersAlbumSavedDto
    suspend fun getCurrentUserPlaylists(accessToken: String): CurrentUsersPlaylistsDto
    suspend fun getUserSavedShows(accessToken: String): UsersSavedShowsDto
    suspend fun getUserSavedEpisodes(accessToken: String): UserSavedEpisodesDto
}


