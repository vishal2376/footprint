package com.nullappstudios.footprint.di

import androidx.room.RoomDatabase
import com.nullappstudios.footprint.data.datasource.IOSLocationDataSource
import com.nullappstudios.footprint.data.datasource.LocationDataSource
import com.nullappstudios.footprint.data.local.AppDatabase
import com.nullappstudios.footprint.data.local.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
	single<LocationDataSource> { IOSLocationDataSource() }
	single<RoomDatabase.Builder<AppDatabase>> { getDatabaseBuilder() }
}
