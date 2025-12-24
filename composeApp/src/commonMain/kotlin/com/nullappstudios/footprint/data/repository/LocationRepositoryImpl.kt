package com.nullappstudios.footprint.data.repository

import com.nullappstudios.footprint.data.datasource.LocationDataSource
import com.nullappstudios.footprint.domain.model.Location
import com.nullappstudios.footprint.domain.repository.LocationRepository

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource
) : LocationRepository {

    override suspend fun getCurrentLocation(): Result<Location> {
        return try {
            val location = locationDataSource.getCurrentLocation()
            Result.success(location)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
