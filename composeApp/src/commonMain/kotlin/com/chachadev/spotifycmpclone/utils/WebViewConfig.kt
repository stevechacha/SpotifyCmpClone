package com.chachadev.spotifycmpclone.utils

import com.multiplatform.webview.web.NativeWebView

/**
 * Platform-specific function to configure WebView settings for Android
 * This is needed because DOM storage settings aren't exposed in the multiplatform API
 */
expect fun configureWebViewForAuth(webView: NativeWebView)

