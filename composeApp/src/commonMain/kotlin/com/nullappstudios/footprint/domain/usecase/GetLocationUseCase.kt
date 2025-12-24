package com.nullappstudios.footprint.domain.usecase

import com.nullappstudios.footprint.domain.model.Location
import com.nullappstudios.footprint.domain.repository.LocationRepository

class GetLocationUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Result<Location> {
        return locationRepository.getCurrentLocation()
    }
}
