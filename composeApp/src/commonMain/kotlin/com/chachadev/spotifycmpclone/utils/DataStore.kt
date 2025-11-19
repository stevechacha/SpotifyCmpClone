package com.chachadev.spotifycmpclone.utils

expect suspend fun Context?.putData(key: String, `object`: String)

expect suspend fun Context?.getData(key: String): String?