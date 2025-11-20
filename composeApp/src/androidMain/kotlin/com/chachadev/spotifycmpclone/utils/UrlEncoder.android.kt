package com.chachadev.spotifycmpclone.utils

import java.net.URLEncoder

actual fun encodeUrlComponent(component: String): String {
    return URLEncoder.encode(component, "UTF-8")
}




