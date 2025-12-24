package com.nullappstudios.footprint.di

import com.nullappstudios.footprint.data.repository.LocationRepositoryImpl
import com.nullappstudios.footprint.domain.repository.LocationRepository
import com.nullappstudios.footprint.domain.usecase.GetLocationUseCase
import com.nullappstudios.footprint.presentation.viewmodel.LocationViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule: Module

val dataModule = module {
    single<LocationRepository> { LocationRepositoryImpl(get()) }
}

val domainModule = module {
    factory { GetLocationUseCase(get()) }
}

val presentationModule = module {
    viewModel { LocationViewModel(get()) }
}

val appModules = listOf(platformModule, dataModule, domainModule, presentationModule)
