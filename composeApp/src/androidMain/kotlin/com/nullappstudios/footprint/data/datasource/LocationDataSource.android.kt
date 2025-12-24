package com.nullappstudios.footprint.data.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.nullappstudios.footprint.domain.model.Location
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AndroidLocationDataSource(
    private val context: Context
) : LocationDataSource {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location {
        return suspendCancellableCoroutine { continuation ->
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            
            if (lastKnownLocation != null) {
                continuation.resume(
                    Location(
                        latitude = lastKnownLocation.latitude,
                        longitude = lastKnownLocation.longitude
                    )
                )
            } else {
                continuation.resumeWithException(Exception("Location not available"))
            }
        }
    }
}
