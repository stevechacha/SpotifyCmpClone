package com.chachadev.spotifycmpclone.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chachadev.core.data.auth.AuthManager
import com.chachadev.spotifycmpclone.presentation.ui.component.CoilImage
import com.chachadev.spotifycmpclone.presentation.viewmodel.ProfileViewModel
import com.chachadev.spotifycmpclone.utils.clearStoredAuthCode
import com.chachadev.spotifycmpclone.utils.getStoredAuthCode
import com.chachadev.spotifycmpclone.utils.rememberBrowserLauncher
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val authManager: AuthManager = koinInject()
    val uriHandler = LocalUriHandler.current
    val browserLauncher = rememberBrowserLauncher()
    val scope = rememberCoroutineScope()
    
    // State to show OAuth WebView
    var showOAuthWebView by remember { mutableStateOf(false) }
    var signInUrl by remember { mutableStateOf<String?>(null) }
    
    // Get stored auth code in composable context
    val storedCode = getStoredAuthCode()
    var processedCode by remember { mutableStateOf<String?>(null) }
    
    // Clear the code immediately after reading it (in composable context)
    if (storedCode != null && storedCode != processedCode) {
        // Mark as processed and clear it
        processedCode = storedCode
        clearStoredAuthCode()
    }
    
    // Handle OAuth flow
    LaunchedEffect(processedCode) {
        // Check if we have a processed code to exchange
        if (processedCode != null) {
            val codeToUse = processedCode!!
            // Exchange code for token
            scope.launch {
                viewModel.handleAuthCallback(codeToUse)
            }
        }
    }
    
    // Set up OAuth flow callback - use WebView instead of external browser
    LaunchedEffect(Unit) {
        viewModel.onStartOAuthFlow = { url ->
            signInUrl = url
            showOAuthWebView = true
        }
    }
    
    // Get redirect URI from AuthManager
    val redirectUri = remember {
        authManager.getRedirectUri()
    }
    
    // Show OAuth WebView if needed
    if (showOAuthWebView && signInUrl != null) {
        OAuthWebViewScreen(
            signInUrl = signInUrl!!,
            redirectUri = redirectUri,
            viewModel = viewModel,
            onAuthSuccess = {
                showOAuthWebView = false
                signInUrl = null
            },
            onAuthFailure = { error ->
                showOAuthWebView = false
                signInUrl = null
            }
        )
        return
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = if (uiState.isLoggedIn && uiState.user != null) Arrangement.Top else Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            if (uiState.isLoggedIn) {
                if (uiState.user != null) {
                    // User Profile Section
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Profile Image
                        CoilImage(
                            imageUrl = uiState.user?.images?.firstOrNull()?.url,
                            contentDescription = uiState.user?.displayName ?: "Profile picture",
                            modifier = Modifier.size(120.dp),
                            contentScale = ContentScale.Crop,
                            shape = CircleShape
                        )
                        
                        // Username/Display Name
                        uiState.user?.displayName?.let { displayName ->
                            Text(
                                text = displayName,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )
                        }
                        
                        // User ID
                        uiState.user?.id?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                        
                        // Email (if available)
                        uiState.user?.email?.let { email ->
                            Text(
                                text = email,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                        
                        // Followers (if available)
                        uiState.user?.followers?.let { followers ->
                            Text(
                                text = "$followers followers",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    // Logged in but profile unavailable
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "You are logged in!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        
                        if (uiState.error != null) {
                            Text(
                                text = uiState.error?:"",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            } else {
                // Login/Register Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Sign in to Spotify",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = "Sign in to sync your library, playlists, and access your saved content.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    if (uiState.error != null) {
                        Text(
                            text = "Error: ${uiState.error}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    Button(
                        onClick = { viewModel.login() },
                        modifier = Modifier.padding(top = 8.dp),
                        enabled = !uiState.isLoading
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(end = 8.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Text("Sign in with Spotify")
                        }
                    }
                }
            }
        }
    }
}

