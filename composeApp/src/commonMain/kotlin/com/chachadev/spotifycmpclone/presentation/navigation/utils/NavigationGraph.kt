package com.chachadev.spotifycmpclone.presentation.navigation.utils

import kotlinx.serialization.Serializable


sealed interface NavigationGraph {
    @Serializable data object Loading : NavigationGraph
    @Serializable data object Auth : NavigationGraph
    @Serializable data object Security : NavigationGraph
    @Serializable data object App : NavigationGraph
    @Serializable data object Welcome : NavigationGraph
    @Serializable data object Profile : NavigationGraph
    @Serializable data object Parcel : NavigationGraph

}
