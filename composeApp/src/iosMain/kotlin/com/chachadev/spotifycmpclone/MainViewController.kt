package com.chachadev.spotifycmpclone

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.chachadev.spotifycmpclone.di.PlatformSpotifyCredentials
import com.chachadev.spotifycmpclone.di.SpotifyCredentials
import com.chachadev.spotifycmpclone.utils.storeAuthCode
import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents

private var credentialsInitialized = false

fun MainViewController() = ComposeUIViewController {
    if (!credentialsInitialized) {
        PlatformSpotifyCredentials.set(
            SpotifyCredentials(
                clientId = infoValue("SpotifyClientID"),
                clientSecret = infoValue("SpotifyClientSecret"),
                redirectUri = infoValue("SpotifyRedirectURI")
            )
        )
        credentialsInitialized = true
    }
    App()
}
fun handleCallbackUrl(urlString: String) {
    // Check if this is our callback URL
    if (urlString.startsWith("spotifycmpclone://callback")) {
        val components = NSURLComponents.componentsWithString(urlString)
        val queryItems = components?.queryItems
        
        if (queryItems != null) {
            // Extract the authorization code from query parameters
            val itemCount = queryItems.count().toInt()
            for (i in 0 until itemCount) {
                val item = queryItems.get(i) as? platform.Foundation.NSURLQueryItem
                if (item?.name == "code") {
                    val code = item.value
                    if (code != null) {
                        storeAuthCode(code)
                        return
                    }
                }
            }
        }
    }
}

private fun infoValue(key: String): String {
    val dictionary = NSBundle.mainBundle.infoDictionary
    return dictionary?.get(key) as? String ?: ""
}