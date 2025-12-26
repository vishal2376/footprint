package com.nullappstudios.footprint.data.datasource

import com.nullappstudios.footprint.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {
    fun getLocationUpdates(): Flow<Location>
    fun stopLocationUpdates()
}
