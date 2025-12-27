package com.nullappstudios.footprint.domain.repository

import com.nullappstudios.footprint.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
	fun getLocationUpdates(): Flow<Location>
	fun stopLocationUpdates()
}
