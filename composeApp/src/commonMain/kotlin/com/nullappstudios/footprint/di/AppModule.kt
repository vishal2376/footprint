package com.nullappstudios.footprint.di

import com.nullappstudios.footprint.data.datasource.OsmTileStreamProvider
import com.nullappstudios.footprint.data.repository.LocationRepositoryImpl
import com.nullappstudios.footprint.domain.repository.LocationRepository
import com.nullappstudios.footprint.domain.usecase.GetLocationUseCase
import com.nullappstudios.footprint.presentation.viewmodel.LocationViewModel
import com.nullappstudios.footprint.presentation.viewmodel.MapViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ovh.plrapps.mapcompose.core.TileStreamProvider

expect val platformModule: Module

val networkModule = module {
    single { HttpClient() }
}

val dataModule = module {
    single<LocationRepository> { LocationRepositoryImpl(get()) }
    single<TileStreamProvider> { OsmTileStreamProvider(get()) }
}

val domainModule = module {
    factory { GetLocationUseCase(get()) }
}

val presentationModule = module {
    viewModel { LocationViewModel(get()) }
    viewModel { MapViewModel(get(), get()) }
}

val appModules = listOf(platformModule, networkModule, dataModule, domainModule, presentationModule)
