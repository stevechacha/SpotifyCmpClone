package com.chachadev.spotifycmpclone.utils

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform