package com.chachadev.core.network.di

import com.chachadev.core.network.HttpClientFactory
import org.koin.dsl.module


val networkModule = module {
    single {
        HttpClientFactory.create()
    }
}