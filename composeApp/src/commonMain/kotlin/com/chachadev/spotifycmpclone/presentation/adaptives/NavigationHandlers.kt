package com.chachadev.spotifycmpclone.presentation.adaptives

import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.chachadev.spotifycmpclone.presentation.navigation.Screen

/**
 * Handles navigation to detail screens, checking if we're navigating to a different item
 * of the same type (e.g., different album) and popping the current screen if needed.
 */
fun navigateToDetail(
    destination: Screen,
    detailNavController: NavHostController
) {
    // Check if we're navigating to a different item of the same type
    val currentBackStackEntry = detailNavController.currentBackStackEntry
    val currentRoute = currentBackStackEntry?.destination?.route ?: ""
    
    val isSameTypeDifferentId = when {
        destination is Screen.App.Album && currentRoute.contains("Album", ignoreCase = true) -> {
            try {
                val currentAlbum = currentBackStackEntry?.toRoute<Screen.App.Album>()
                currentAlbum?.albumId != destination.albumId
            } catch (e: Exception) {
                false
            }
        }
        destination is Screen.App.Artist && currentRoute.contains("Artist", ignoreCase = true) -> {
            try {
                val currentArtist = currentBackStackEntry?.toRoute<Screen.App.Artist>()
                currentArtist?.artistId != destination.artistId
            } catch (e: Exception) {
                false
            }
        }
        destination is Screen.App.Playlist && currentRoute.contains("Playlist", ignoreCase = true) -> {
            try {
                val currentPlaylist = currentBackStackEntry?.toRoute<Screen.App.Playlist>()
                currentPlaylist?.playlistId != destination.playlistId
            } catch (e: Exception) {
                false
            }
        }
        else -> false
    }
    
    if (isSameTypeDifferentId) {
        // Pop current screen and navigate to new one
        detailNavController.popBackStack()
        detailNavController.navigate(destination) {
            launchSingleTop = true
        }
    } else {
        detailNavController.navigate(destination) {
            launchSingleTop = true
        }
    }
}

