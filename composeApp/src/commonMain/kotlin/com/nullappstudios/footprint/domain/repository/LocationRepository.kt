package com.nullappstudios.footprint.domain.repository

import com.nullappstudios.footprint.domain.model.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<Location>
}
