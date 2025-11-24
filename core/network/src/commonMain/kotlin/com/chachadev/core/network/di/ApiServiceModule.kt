package com.chachadev.core.network.di

import api.IUserApiService
import com.chachadev.core.network.api.UserApiService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val apiServiceModule = module {
     singleOf(::UserApiService) { bind<IUserApiService>() }
}