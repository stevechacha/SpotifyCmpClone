package com.chachadev.core.network.utils

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

actual fun getClientEngine(): HttpClientEngine {
    return Js.create()
}