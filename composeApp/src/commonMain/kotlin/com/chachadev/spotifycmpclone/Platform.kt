package com.chachadev.spotifycmpclone

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform