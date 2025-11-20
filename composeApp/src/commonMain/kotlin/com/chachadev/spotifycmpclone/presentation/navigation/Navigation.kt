package com.chachadev.spotifycmpclone.presentation.navigation

import kotlinx.serialization.Serializable


sealed interface Screen {
    @Serializable
    data object App : Screen {
        @Serializable data object DashBoard : Screen {
            @Serializable data object Home : Screen
            @Serializable data object Search : Screen
            @Serializable data object Library : Screen
            @Serializable data object Profile : Screen
        }
        @Serializable data class Track(val trackId: String) : Screen
        @Serializable data class Album(val albumId: String) : Screen
        @Serializable data class Artist(val artistId: String) : Screen
        @Serializable data class Playlist(val playlistId: String) : Screen
        @Serializable data object EmptyDetailScreenDestination : Screen
    }

}
/*
@Composable
fun AppNavigation(
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    albumDetailViewModel: AlbumDetailViewModel,
    playlistDetailViewModel: PlaylistDetailViewModel,
    artistDetailViewModel: ArtistDetailViewModel,
    trackDetailViewModel: TrackDetailViewModel,
    profileViewModel: ProfileViewModel,
    currentScreen: Screen = Screen.Home,
    onNavigate: (Screen) -> Unit = {}
) {
    when (currentScreen) {
        is Screen.Home -> {
            HomeScreen(
                viewModel = homeViewModel,
                onAlbumClick = { onNavigate(Album(it)) },
                onPlaylistClick = { onNavigate(Playlist(it)) }
            )
        }
        is Screen.Search -> {
            SearchScreen(
                viewModel = searchViewModel,
                onTrackClick = { onNavigate(Track(it)) },
                onAlbumClick = { onNavigate(Album(it)) },
                onArtistClick = { onNavigate(Artist(it)) },
                onPlaylistClick = { onNavigate(Playlist(it)) }
            )
        }
        is Screen.Library -> {
            LibraryScreen()
        }
        is Screen.Profile -> {
            ProfileScreen(viewModel = profileViewModel)
        }
        is Screen.Track -> {
            TrackScreen(
                trackId = currentScreen.trackId,
                viewModel = trackDetailViewModel,
                onBack = { onNavigate(Screen.Search) }
            )
        }
        is Screen.Album -> {
            AlbumScreen(
                albumId = currentScreen.albumId,
                viewModel = albumDetailViewModel,
                onTrackSelected = { onNavigate(Track(it)) },
                onBack = { onNavigate(Screen.Search) }
            )
        }
        is Screen.Artist -> {
            ArtistScreen(
                artistId = currentScreen.artistId,
                viewModel = artistDetailViewModel,
                onTrackSelected = { onNavigate(Track(it)) },
                onBack = { onNavigate(Screen.Search) }
            )
        }
        is Screen.Playlist -> {
            PlaylistScreen(
                playlistId = currentScreen.playlistId,
                viewModel = playlistDetailViewModel,
                onTrackSelected = { onNavigate(Track(it)) },
                onBack = { onNavigate(Screen.Search) }
            )
        }

    }
}*/

