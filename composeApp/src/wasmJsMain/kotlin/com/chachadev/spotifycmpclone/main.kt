package com.chachadev.spotifycmpclone

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.chachadev.core.network.BuildKonfig
import com.chachadev.spotifycmpclone.di.PlatformSpotifyCredentials
import com.chachadev.spotifycmpclone.di.SpotifyCredentials
import com.chachadev.spotifycmpclone.utils.storeAuthCode
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Element

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
   val clientId: String = BuildKonfig.CLIENT_ID
   val clientSecret: String = BuildKonfig.CLIENT_SECRET
   val redirectUri: String = "http://localhost:3000/callback"
    PlatformSpotifyCredentials.set(
        SpotifyCredentials(
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUri = redirectUri
        )
    )
    
    // Handle OAuth callback - extract code from URL
    handleOAuthCallback()
    
    ComposeViewport {
        App()
    }
}

private fun handleOAuthCallback() {
    val currentUrl = window.location.href
    if (currentUrl.contains("callback")) {
        try {
            val search = window.location.search
            if (search.isNotEmpty()) {
                // Parse query parameters manually to avoid type issues
                val code = extractQueryParameter(search, "code")
                if (code != null) {
                    // Store the authorization code
                    storeAuthCode(code)
                    // Remove the code from URL to clean up
                    val cleanUrl = window.location.origin + window.location.pathname
                    window.history.replaceState(null, "", cleanUrl)
                }
            }
        } catch (e: Exception) {
            // Error handling - could log if needed
        }
    }
}

private fun extractQueryParameter(search: String, paramName: String): String? {
    if (search.isEmpty()) return null
    
    // Remove the leading '?'
    val queryString = search.removePrefix("?")
    if (queryString.isEmpty()) return null
    
    // Split by '&' to get individual parameters
    val params = queryString.split("&")
    for (param in params) {
        if (param.isEmpty()) continue
        
        val parts = param.split("=", limit = 2)
        if (parts.size == 2 && parts[0] == paramName) {
            val encodedValue = parts[1]
            if (encodedValue.isEmpty()) return ""
            
            // Decode the value (handle URL encoding)
            // Use decodeURIComponent for URL-encoded values, not atob (which is for base64)
           /* return try {
                // Use js() to call JavaScript's decodeURIComponent function through window
                val decoder = js("window.decodeURIComponent")
                decoder(encodedValue) as String
            } catch (e: Exception) {
                // If decoding fails, return the raw value
                encodedValue
            }*/
        }
    }
    return null
}

private fun configValue(key: String): String {
    val element = document.getElementById("spotify-config") as? Element
    return element?.getAttribute("data-$key") ?: ""
}