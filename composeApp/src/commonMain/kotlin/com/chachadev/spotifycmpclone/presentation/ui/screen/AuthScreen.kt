package com.chachadev.spotifycmpclone.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.chachadev.spotifycmpclone.data.auth.AuthManager
import com.chachadev.spotifycmpclone.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

expect fun getStoredAuthCode(): String?
expect fun clearStoredAuthCode()

@Composable
fun AuthScreen(
    viewModel: ProfileViewModel,
    onAuthSuccess: () -> Unit,
    onAuthFailure: (String) -> Unit = {}
) {
    val authManager: AuthManager = koinInject()
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        // Check if we have a stored auth code (from callback)
        val storedCode = getStoredAuthCode()
        if (storedCode != null) {
            // We have an auth code, exchange it for token
            scope.launch {
                viewModel.handleAuthCallback(storedCode)
                clearStoredAuthCode()
            }
        } else {
            // No code, open browser for authentication
            val signInUrl = authManager.getSignInUrl()
            try {
                uriHandler.openUri(signInUrl)
                isLoading = true
            } catch (e: Exception) {
                errorMessage = "Cannot open browser: ${e.message}"
            }
        }
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Opening browser for authentication...",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Please complete authentication in the browser and return to this app.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            errorMessage?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// Function to extract authorization code from redirect URL
/*
fun extractAuthorizationCode(redirectUrl: String): String? {
    return try {
        val url = java.net.URL(redirectUrl)
        val query = url.query ?: return null
        val params = query.split("&")
        params.firstOrNull { it.startsWith("code=") }?.substringAfter("code=")
    } catch (e: Exception) {
        null
    }
}
*/

