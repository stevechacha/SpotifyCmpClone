package com.chachadev.core.network.utils

import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.HttpClientEngine

actual fun getClientEngine(): HttpClientEngine {
    return OkHttp.create()

}