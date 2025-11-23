package com.chachadev.core.network

import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.HttpClientEngine

actual fun getClientEngine(): HttpClientEngine {
    return OkHttp.create()

}