package com.chachadev.spotifycmpclone.di

import com.chachadev.spotifycmpclone.data.api.SpotifyApi
import com.chachadev.spotifycmpclone.data.network.HttpClientFactory
import com.chachadev.spotifycmpclone.data.repository.SpotifyRepositoryImpl
import com.chachadev.spotifycmpclone.domain.repository.SpotifyRepository
import com.chachadev.spotifycmpclone.domain.usecase.GetArtistTopTracksUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetFeaturedPlaylistsUseCase
import com.chachadev.spotifycmpclone.domain.usecase.GetNewReleasesUseCase
import com.chachadev.spotifycmpclone.domain.usecase.SearchUseCase
import com.chachadev.spotifycmpclone.presentation.viewmodel.HomeViewModel
import com.chachadev.spotifycmpclone.presentation.viewmodel.SearchViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun appModule() = module {
    includes(
        apiModule,
        repoModule,
        useCaseModule,
        screenViewModelModule
    )


}


fun initKoin(
    clientId: String = "76a675416313462c92babb568e064676",
    clientSecret: String = "25cb1fc758d14074be471a1c3cb45349",
    redirectUri: String = "http://localhost:3000/callback",
    config: KoinAppDeclaration? = null
) {
    startKoin {
        config?.invoke(this)
        properties(
            mapOf(
                "spotify_client_id" to clientId,
                "spotify_client_secret" to clientSecret,
                "spotify_redirect_uri" to redirectUri
            )
        )
        modules(appModule())
    }
}
