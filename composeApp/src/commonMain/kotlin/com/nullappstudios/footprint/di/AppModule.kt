package com.nullappstudios.footprint.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.nullappstudios.footprint.data.datasource.FoggedTileStreamProvider
import com.nullappstudios.footprint.data.local.AppDatabase
import com.nullappstudios.footprint.data.repository.ExploredTileRepositoryImpl
import com.nullappstudios.footprint.data.repository.LocationRepositoryImpl
import com.nullappstudios.footprint.data.repository.TrackRepositoryImpl
import com.nullappstudios.footprint.domain.repository.ExploredTileRepository
import com.nullappstudios.footprint.domain.repository.LocationRepository
import com.nullappstudios.footprint.domain.repository.TrackRepository
import com.nullappstudios.footprint.domain.usecase.GetLiveLocationUseCase
import com.nullappstudios.footprint.presentation.home_screen.viewmodel.HomeViewModel
import com.nullappstudios.footprint.presentation.map_screen.viewmodel.MapViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
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
			.fallbackToDestructiveMigration(dropAllTables = true)
			.setDriver(BundledSQLiteDriver())
			.setJournalMode(androidx.room.RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
			.setQueryCoroutineContext(Dispatchers.IO)
			.build()
	}
	single { get<AppDatabase>().trackDao() }
	single { get<AppDatabase>().exploredTileDao() }
}

val dataModule = module {
	single<LocationRepository> { LocationRepositoryImpl(get()) }
	single<TrackRepository> { TrackRepositoryImpl(get()) }
	single<ExploredTileRepository> { ExploredTileRepositoryImpl(get()) }

	// Shared state for explored tiles - used by TileStreamProvider
	single { MutableStateFlow<Set<String>>(emptySet()) }

	single<TileStreamProvider> {
		FoggedTileStreamProvider(
			httpClient = get(),
			exploredTilesFlow = get<MutableStateFlow<Set<String>>>()
		)
	}
}

val domainModule = module {
	factory { GetLiveLocationUseCase(get()) }
}

val presentationModule = module {
	viewModel { HomeViewModel(lazy { get() }, lazy { get() }) }
	viewModel {
		MapViewModel(
			getLiveLocationUseCase = get(),
			tileStreamProvider = get(),
			trackRepository = lazy { get() },
			exploredTileRepository = lazy { get() },
			exploredTilesStateFlow = get()
		)
	}
}

val appModules = listOf(platformModule, networkModule, databaseModule, dataModule, domainModule, presentationModule)
