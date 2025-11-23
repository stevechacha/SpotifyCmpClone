package com.chachadev.core.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp // Platform-specific engine

actual fun getClientEngine(): HttpClientEngine {
    return OkHttp.create()
}