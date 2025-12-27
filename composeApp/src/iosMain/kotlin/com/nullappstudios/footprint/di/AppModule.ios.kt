package com.nullappstudios.footprint.di

import com.nullappstudios.footprint.data.datasource.IOSLocationDataSource
import com.nullappstudios.footprint.data.datasource.LocationDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
	single<LocationDataSource> { IOSLocationDataSource() }
}
