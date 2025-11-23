package com.chachadev.core.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

actual fun getClientEngine(): HttpClientEngine {
    return Js.create()
}