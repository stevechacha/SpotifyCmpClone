package com.chachadev.spotifycmpclone.utils

import kotlinx.browser.window

actual fun encodeUrlComponent(component: String): String {
    return window.encodeURIComponent(component)
}




