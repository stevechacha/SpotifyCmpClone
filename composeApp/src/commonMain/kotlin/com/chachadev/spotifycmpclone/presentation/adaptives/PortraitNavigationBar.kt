package com.chachadev.spotifycmpclone.presentation.adaptives

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.chachadev.core.common.screen.Portrait
import com.chachadev.core.common.screen.ScreenOrientation
import com.chachadev.spotifycmpclone.presentation.navigation.Screen

@Composable
fun PortraitNavigationBar(
    orientation: ScreenOrientation,
    shouldShow: Boolean,
    currentListScreen: Screen,
    onCurrentListScreenChange: (Screen) -> Unit,
    listNavController: NavHostController
) {
    if (shouldShow && orientation is Portrait) {
        val navigationItems = remember {
            listOf(
                NavigationItem("Home", Icons.Default.Home, Screen.App.DashBoard.Home),
                NavigationItem("Search", Icons.Default.Search, Screen.App.DashBoard.Search),
                NavigationItem("Library", Icons.Default.LibraryMusic, Screen.App.DashBoard.Library),
                NavigationItem("Profile", Icons.Default.Person, Screen.App.DashBoard.Profile)
            )
        }



        NavigationBar {
            navigationItems.forEach { destination ->
                val isSelected = when (destination.screen) {
                    Screen.App.DashBoard.Home -> currentListScreen is Screen.App.DashBoard.Home
                    Screen.App.DashBoard.Search -> currentListScreen is Screen.App.DashBoard.Search
                    Screen.App.DashBoard.Library -> currentListScreen is Screen.App.DashBoard.Library
                    Screen.App.DashBoard.Profile -> currentListScreen is Screen.App.DashBoard.Profile
                    else -> false
                }
                NavigationBarItem(
                    icon = { Icon(destination.icon, contentDescription = destination.label) },
                    label = { Text(destination.label) },
                    selected = isSelected,
                    onClick = {
                        if (currentListScreen != destination.screen) {
                            onCurrentListScreenChange(destination.screen)
                            when (destination.screen) {
                                Screen.App.DashBoard.Home -> {
                                    listNavController.navigate(Screen.App.DashBoard.Home) {
                                        popUpTo(listNavController.graph.findStartDestination().id) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                }
                                Screen.App.DashBoard.Search -> {
                                    listNavController.navigate(Screen.App.DashBoard.Search) {
                                        popUpTo(listNavController.graph.findStartDestination().id) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                }
                                Screen.App.DashBoard.Library -> {
                                    listNavController.navigate(Screen.App.DashBoard.Library) {
                                        popUpTo(listNavController.graph.findStartDestination().id) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                }
                                Screen.App.DashBoard.Profile -> {
                                    listNavController.navigate(Screen.App.DashBoard.Profile) {
                                        popUpTo(listNavController.graph.findStartDestination().id) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                }
                                else -> Unit
                            }
                        }
                    }
                )
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)