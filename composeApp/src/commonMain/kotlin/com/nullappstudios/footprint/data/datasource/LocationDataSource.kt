package com.nullappstudios.footprint.data.datasource

import com.nullappstudios.footprint.domain.model.Location

interface LocationDataSource {
    suspend fun getCurrentLocation(): Location
}
