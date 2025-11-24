package com.chachadev.core.network.di

import com.chachadev.core.network.api.NetworkApi
import com.chachadev.core.network.datasource.RemoteDataSource
import com.chachadev.core.network.datasource.RemoteDataSourceImpl
import org.koin.dsl.module

val remoteDataSourceModule = module {
    single<NetworkApi> { NetworkApi(client = get()) }   // assumes HttpClient is already provided
    single<RemoteDataSource> {
        RemoteDataSourceImpl(apiService = get())
    }
}