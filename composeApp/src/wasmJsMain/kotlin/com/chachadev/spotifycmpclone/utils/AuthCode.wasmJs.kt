package com.chachadev.spotifycmpclone.utils

import kotlinx.browser.window

actual fun storeAuthCode(code: String) {
    window.localStorage.setItem(AUTH_CODE_KEY, code)
}