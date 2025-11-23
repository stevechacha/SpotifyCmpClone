package com.chachadev.spotifycmpclone.domain.repository

import com.chachadev.spotifycmpclone.domain.model.Album
import com.chachadev.spotifycmpclone.domain.model.Artist
import com.chachadev.spotifycmpclone.domain.model.Episode
import com.chachadev.spotifycmpclone.domain.model.Playlist
import com.chachadev.spotifycmpclone.domain.model.SearchResult
import com.chachadev.spotifycmpclone.domain.model.Show
import com.chachadev.spotifycmpclone.domain.model.Track
import com.chachadev.spotifycmpclone.domain.model.User

interface SpotifyRepository {
    suspend fun search(query: String, type: String = "track,album,artist,playlist"): Result<SearchResult>
    suspend fun getTrack(id: String): Result<Track>
    suspend fun getAlbum(id: String): Result<Album>
    suspend fun getArtist(id: String): Result<Artist>
    suspend fun getPlaylist(id: String): Result<Playlist>
    suspend fun getNewReleases(limit: Int = 20, offset: Int = 0): Result<List<Album>>
    suspend fun getFeaturedPlaylists(limit: Int = 20, offset: Int = 0): Result<List<Playlist>>
    suspend fun getArtistTopTracks(artistId: String): Result<List<Track>>
    suspend fun getAlbumTracks(albumId: String): Result<List<Track>>
    suspend fun getPlaylistTracks(playlistId: String): Result<List<Track>>
    suspend fun getShow(showId: String): Result<Show>
    suspend fun getShowEpisodes(showId: String, limit: Int = 50, offset: Int = 0): Result<List<Episode>>
    suspend fun getCurrentUser(): Result<User>
    suspend fun getRecentlyPlayedTracks(limit: Int = 20): Result<List<Track>>
}

