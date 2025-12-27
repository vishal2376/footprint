package com.nullappstudios.footprint.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.nullappstudios.footprint.data.datasource.OsmTileStreamProvider
import com.nullappstudios.footprint.data.local.AppDatabase
import com.nullappstudios.footprint.data.repository.LocationRepositoryImpl
import com.nullappstudios.footprint.data.repository.TrackRepositoryImpl
import com.nullappstudios.footprint.domain.repository.LocationRepository
import com.nullappstudios.footprint.domain.repository.TrackRepository
import com.nullappstudios.footprint.domain.usecase.GetLiveLocationUseCase
import com.nullappstudios.footprint.presentation.home_screen.viewmodel.HomeViewModel
import com.nullappstudios.footprint.presentation.map_screen.viewmodel.MapViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ovh.plrapps.mapcompose.core.TileStreamProvider

expect val platformModule: Module

val networkModule = module {
	single { HttpClient() }
}

val databaseModule = module {
	single<AppDatabase> {
		get<androidx.room.RoomDatabase.Builder<AppDatabase>>()
			.setDriver(BundledSQLiteDriver())
			.setQueryCoroutineContext(Dispatchers.IO)
			.build()
	}
	single { get<AppDatabase>().trackDao() }
}

val dataModule = module {
	single<LocationRepository> { LocationRepositoryImpl(get()) }
	single<TileStreamProvider> { OsmTileStreamProvider(get()) }
	single<TrackRepository> { TrackRepositoryImpl(get()) }
}

val domainModule = module {
	factory { GetLiveLocationUseCase(get()) }
}

val presentationModule = module {
	viewModel { HomeViewModel() }
	viewModel { MapViewModel(get(), get(), get()) }
}

val appModules = listOf(platformModule, networkModule, databaseModule, dataModule, domainModule, presentationModule)
