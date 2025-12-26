package com.nullappstudios.footprint.data.repository

import com.nullappstudios.footprint.data.datasource.LocationDataSource
import com.nullappstudios.footprint.domain.model.Location
import com.nullappstudios.footprint.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource
) : LocationRepository {

    override fun getLocationUpdates(): Flow<Location> {
        return locationDataSource.getLocationUpdates()
    }

    override fun stopLocationUpdates() {
        locationDataSource.stopLocationUpdates()
    }
}
