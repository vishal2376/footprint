package com.nullappstudios.footprint.data.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.nullappstudios.footprint.domain.model.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AndroidLocationDataSource(
    context: Context
) : LocationDataSource {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    
    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            UPDATE_INTERVAL_MS
        ).apply {
            setMinUpdateIntervalMillis(MIN_UPDATE_INTERVAL_MS)
            setMinUpdateDistanceMeters(MIN_UPDATE_DISTANCE_METERS)
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { androidLocation ->
                    trySend(
                        Location(
                            latitude = androidLocation.latitude,
                            longitude = androidLocation.longitude
                        )
                    )
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )

        awaitClose {
            locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
            locationCallback = null
        }
    }

    override fun stopLocationUpdates() {
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
        locationCallback = null
    }

    companion object {
        private const val UPDATE_INTERVAL_MS = 3000L
        private const val MIN_UPDATE_INTERVAL_MS = 1500L
        private const val MIN_UPDATE_DISTANCE_METERS = 5f
    }
}
