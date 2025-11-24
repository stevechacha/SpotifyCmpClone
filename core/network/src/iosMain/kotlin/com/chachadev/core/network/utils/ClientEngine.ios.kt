package com.chachadev.core.network.utils

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun getClientEngine(): HttpClientEngine {
    return Darwin.create()
}