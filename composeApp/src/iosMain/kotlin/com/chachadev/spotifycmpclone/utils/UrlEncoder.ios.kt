package com.chachadev.spotifycmpclone.utils

import platform.Foundation.NSString
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters
import platform.Foundation.NSCharacterSet
import platform.Foundation.URLQueryAllowedCharacterSet

actual fun encodeUrlComponent(component: String): String {
    val nsString = component as NSString
    val allowedCharacters = NSCharacterSet.URLQueryAllowedCharacterSet
    return nsString.stringByAddingPercentEncodingWithAllowedCharacters(allowedCharacters) ?: component
}




