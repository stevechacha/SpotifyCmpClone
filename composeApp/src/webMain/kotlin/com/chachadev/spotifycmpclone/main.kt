package com.chachadev.spotifycmpclone

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.chachadev.spotifycmpclone.di.PlatformSpotifyCredentials
import com.chachadev.spotifycmpclone.di.SpotifyCredentials
import kotlinx.browser.document
import org.w3c.dom.Element

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
   val clientId: String = "76a675416313462c92babb568e064691"
   val clientSecret: String = "25cb1fc758d14074be471a1c3cb45349"
   val redirectUri: String = "http://localhost:3000/callback"
    PlatformSpotifyCredentials.set(
        SpotifyCredentials(
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUri = redirectUri
        )
    )
    ComposeViewport {
        App()
    }
}

private fun configValue(key: String): String {
    val element = document.getElementById("spotify-config") as? Element
    return element?.getAttribute("data-$key") ?: ""
}