package com.chachadev.spotifycmpclone.utils

import com.chachadev.core.common.config.ClientConfig
import kotlinx.serialization.json.Json

fun configuration(): ClientConfig = serializer.decodeFromString(ClientConfig.serializer(), json)

const val json = """
{
  "api": "http://localhost:8080",
  "start": "",
  "window": "2k"
}
"""

private val serializer by lazy {
    Json {
        ignoreUnknownKeys = true
    }
}