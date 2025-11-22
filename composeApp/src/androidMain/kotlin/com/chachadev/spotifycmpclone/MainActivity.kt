package com.chachadev.spotifycmpclone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.chachadev.spotifycmpclone.utils.storeAuthCode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        handleAuthDeeplink(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleAuthDeeplink(intent)
    }

    private fun handleAuthDeeplink(intent: Intent) {
        val data: Uri? = intent.data
        if (data != null) {
            val scheme = data.scheme
            val host = data.host
            val path = data.path
            
            // Check if this is our Spotify OAuth callback
            if (scheme == "spotifycmpclone" && host == "callback") {
                // Extract the authorization code from query parameters
                val code = data.getQueryParameter("code")
                if (code != null) {
                    // Store the authorization code
                    storeAuthCode(this, code)
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}