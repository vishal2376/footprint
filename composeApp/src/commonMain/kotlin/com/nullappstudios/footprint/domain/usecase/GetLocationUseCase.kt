package com.nullappstudios.footprint.domain.usecase

import com.nullappstudios.footprint.domain.model.Location
import com.nullappstudios.footprint.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class GetLiveLocationUseCase(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<Location> {
        return locationRepository.getLocationUpdates()
    }

    fun stop() {
        locationRepository.stopLocationUpdates()
    }
}
