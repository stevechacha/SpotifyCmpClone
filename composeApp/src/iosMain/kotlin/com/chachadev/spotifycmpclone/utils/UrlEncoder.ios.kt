package com.chachadev.spotifycmpclone.utils

import platform.Foundation.NSString
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters
import platform.Foundation.NSCharacterSet

actual fun encodeUrlComponent(component: String): String {
    val nsString = NSString.create(string = component)
    val allowedCharacters = NSCharacterSet.URLQueryAllowedCharacterSet
    return nsString.stringByAddingPercentEncodingWithAllowedCharacters(allowedCharacters) ?: component
}



