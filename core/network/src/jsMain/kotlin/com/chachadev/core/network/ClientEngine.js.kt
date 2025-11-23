package com.chachadev.core.network

import io.ktor.client.engine.js.Js
import io.ktor.client.engine.HttpClientEngine


actual fun getClientEngine(): HttpClientEngine {
    return Js.create()
}