package com.chachadev.spotifycmpclone.presentation.navigation.utils

import androidx.navigation.NavHostController
import com.chachadev.spotifycmpclone.presentation.navigation.Screen

fun NavHostController.onNavigate(destination: Screen, finish: Boolean = false) {
    println("🚀 NavigationUtils: onNavigate called with destination: $destination, finish: $finish")
    navigate(destination) {
        if (finish) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }
    println("🚀 NavigationUtils: navigate call completed")
}


fun NavHostController.onNavigateBack(destination: Screen) {
    popBackStack(destination, inclusive = false)
}


fun NavHostController.onNavigateGraph(graph: NavigationGraph, finish: Boolean = false) {
    navigate(graph) {
        if (finish) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }
}

fun NavHostController.onGoBack() {
    popBackStack()
}