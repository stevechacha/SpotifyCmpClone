package com.chachadev.spotifycmpclone.presentation.adaptives

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chachadev.spotifycmpclone.presentation.ui.component.CoilImage
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.chachadev.core.common.screen.Landscape
import com.chachadev.core.common.screen.ScreenOrientation
import com.chachadev.spotifycmpclone.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun LandscapeTopNavigationBar(
    modifier: Modifier = Modifier,
    orientation: ScreenOrientation,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    listNavController: NavHostController,
    detailNavController: NavHostController,
    currentListScreen: Screen,
    onCurrentListScreenChange: (Screen) -> Unit,
    isDetailFlowActive: Boolean
) {
    if (orientation is Landscape) {
        val scope = rememberCoroutineScope()
        
        // Navigate to Search screen when user starts typing
        LaunchedEffect(searchQuery, orientation, isDetailFlowActive) {
            if (searchQuery.isNotEmpty()) {
                if (orientation is Landscape) {
                    // In landscape mode, if we're on a detail screen, navigate back to show SearchScreen
                    if (isDetailFlowActive) {
                        scope.launch {
                            detailNavController.navigate(Screen.App.EmptyDetailScreenDestination) {
                                popUpTo(detailNavController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                } else {
                    // In portrait mode, navigate list pane to Search screen
                    if (currentListScreen != Screen.App.DashBoard.Search) {
                        onCurrentListScreenChange(Screen.App.DashBoard.Search)
                        listNavController.navigate(Screen.App.DashBoard.Search) {
                            popUpTo(listNavController.graph.findStartDestination().id) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
        
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Navigation arrows
            IconButton(
                onClick = { /* Handle back navigation */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            IconButton(
                onClick = { /* Handle forward navigation */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Forward",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Home button
            IconButton(
                onClick = {
                    onCurrentListScreenChange(Screen.App.DashBoard.Home)
                    listNavController.navigate(Screen.App.DashBoard.Home) {
                        popUpTo(listNavController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                placeholder = {
                    Text(
                        text = "What do you want to play?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                // Navigate to Search screen when search icon is clicked
                                onCurrentListScreenChange(Screen.App.DashBoard.Search)
                                listNavController.navigate(Screen.App.DashBoard.Search) {
                                    popUpTo(listNavController.graph.findStartDestination().id) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            },
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Notification bell with badge
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = Color(0xFF1DB954) // Spotify green
                    )
                }
            ) {
                IconButton(
                    onClick = { /* Handle notifications */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            
            // Friends icon (using Person icon as placeholder)
            IconButton(
                onClick = { /* Handle friends */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Friends",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            // User profile picture
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        onCurrentListScreenChange(Screen.App.DashBoard.Profile)
                        listNavController.navigate(Screen.App.DashBoard.Profile) {
                            popUpTo(listNavController.graph.findStartDestination().id) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
            ) {
                CoilImage(
                    imageUrl = null, // TODO: Get from ProfileViewModel
                    contentDescription = "Profile",
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

