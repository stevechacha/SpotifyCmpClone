package com.chachadev.spotifycmpclone.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.chachadev.spotifycmpclone.data.auth.AuthManager
import com.chachadev.spotifycmpclone.presentation.viewmodel.ProfileViewModel
import com.chachadev.spotifycmpclone.utils.configureWebViewForAuth
import com.chachadev.spotifycmpclone.utils.storeAuthCode
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun OAuthWebViewScreen(
    signInUrl: String,
    redirectUri: String,
    viewModel: ProfileViewModel = koinInject(),
    onAuthSuccess: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    val authManager: AuthManager = koinInject()
    val scope = rememberCoroutineScope()
    val navigator: WebViewNavigator = rememberWebViewNavigator()
    val webViewState = rememberWebViewState(signInUrl)
    webViewState.webSettings.apply {
        isJavaScriptEnabled = true
        // Use Android Chrome user agent to avoid CSP issues and match expected browser
        customUserAgentString =
            "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
        androidWebSettings.apply {
            isAlgorithmicDarkeningAllowed = true
            safeBrowsingEnabled = true
            // Additional Android settings for better form handling
            allowFileAccess = true
            allowUniversalAccessFromFileURLs = true
        }
    }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var codeExtracted by remember { mutableStateOf(false) }

    // Intercept navigation to detect callback URL
    LaunchedEffect(webViewState.lastLoadedUrl) {
        val currentUrl = webViewState.lastLoadedUrl ?: return@LaunchedEffect
        
        // Debug logging
        println("OAuthWebViewScreen: URL changed to: $currentUrl")
        println("OAuthWebViewScreen: Looking for redirectUri: $redirectUri")
        
        // Only process if we haven't already extracted the code
        if (codeExtracted) {
            println("OAuthWebViewScreen: Code already extracted, ignoring URL")
            return@LaunchedEffect
        }
        
        // More precise callback URL detection - check if URL starts with redirectUri
        // and contains query parameters (which indicates it's the actual callback, not just a similar URL)
        val isCallbackUrl = currentUrl.startsWith(redirectUri) && 
                           (currentUrl.contains("?code=") || currentUrl.contains("&code=") || currentUrl.contains("?error="))
        
        println("OAuthWebViewScreen: isCallbackUrl = $isCallbackUrl")
        
        if (isCallbackUrl) {
            println("OAuthWebViewScreen: Callback URL detected! Extracting code...")
            codeExtracted = true
            isLoading = true
            
            // Extract authorization code from URL
            val code = extractCodeFromUrl(currentUrl)
            
            if (code != null) {
                // Store auth code for debugging/backup (optional)
                storeAuthCode(code)
                
                // Exchange code for token
                scope.launch {
                    val result = authManager.exchangeCodeForToken(code)
                    result.fold(
                        onSuccess = {
                            // Authentication successful - loadUserProfile is called in ViewModel
                            onAuthSuccess()
                        },
                        onFailure = { error ->
                            errorMessage = error.message ?: "Failed to authenticate"
                            onAuthFailure(errorMessage ?: "Unknown error")
                        }
                    )
                    isLoading = false
                }
            } else {
                // Check if there's an error parameter instead
                val errorParam = extractErrorFromUrl(currentUrl)
                if (errorParam != null) {
                    errorMessage = "Authentication error: $errorParam"
                    onAuthFailure(errorMessage?:"")
                } else {
                    errorMessage = "No authorization code found in callback URL"
                    onAuthFailure(errorMessage?:"")
                }
                isLoading = false
            }
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        WebView(
            state = webViewState,
            navigator = navigator,
            modifier = Modifier.fillMaxSize(),
            onCreated = { webView ->
                // Configure Android-specific settings for DOM storage
                configureWebViewForAuth(webView)
            }
        )
        
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

private fun extractCodeFromUrl(url: String): String? {
    // Parse query parameters from URL
    val queryStart = url.indexOf('?')
    if (queryStart == -1) return null
    
    val queryString = url.substring(queryStart + 1)
    val params = queryString.split("&")
    
    for (param in params) {
        val parts = param.split("=", limit = 2)
        if (parts.size == 2 && parts[0] == "code") {
            return parts[1]
        }
    }
    
    return null
}

private fun extractErrorFromUrl(url: String): String? {
    // Parse error parameter from URL
    val queryStart = url.indexOf('?')
    if (queryStart == -1) return null
    
    val queryString = url.substring(queryStart + 1)
    val params = queryString.split("&")
    
    for (param in params) {
        val parts = param.split("=", limit = 2)
        if (parts.size == 2 && parts[0] == "error") {
            return parts[1]
        }
    }
    
    return null
}

