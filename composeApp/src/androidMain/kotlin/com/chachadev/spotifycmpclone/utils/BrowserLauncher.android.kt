package com.chachadev.spotifycmpclone.utils

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberBrowserLauncher(): BrowserLauncher {
    val context = LocalContext.current
    
    return object : BrowserLauncher {
        override fun openUrl(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
        
        override fun canHandleUrl(url: String): Boolean {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            return intent.resolveActivity(context.packageManager) != null
        }
    }
}



