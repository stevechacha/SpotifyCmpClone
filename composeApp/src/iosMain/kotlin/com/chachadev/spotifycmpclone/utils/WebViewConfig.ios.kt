package com.chachadev.spotifycmpclone.utils

import com.multiplatform.webview.web.NativeWebView

actual fun configureWebViewForAuth(webView: NativeWebView) {
    // iOS WebView (WKWebView) has DOM storage enabled by default
    // No additional configuration needed
}

