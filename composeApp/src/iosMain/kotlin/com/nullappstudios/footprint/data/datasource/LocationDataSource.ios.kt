package com.nullappstudios.footprint.data.datasource

import com.nullappstudios.footprint.domain.model.Location
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLDistanceFilterNone
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject

class IOSLocationDataSource : LocationDataSource {
    
    private var locationManager: CLLocationManager? = null
    
    @OptIn(ExperimentalForeignApi::class)
    override fun getLocationUpdates(): Flow<Location> = callbackFlow {
        locationManager = CLLocationManager().apply {
            desiredAccuracy = kCLLocationAccuracyBest
            distanceFilter = MIN_DISTANCE_METERS
        }
        
        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                val clLocation = didUpdateLocations.lastOrNull() as? CLLocation
                clLocation?.coordinate?.useContents {
                    trySend(
                        Location(
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                }
            }
            
            override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                close(Exception(didFailWithError.localizedDescription))
            }
        }
        
        locationManager?.delegate = delegate
        locationManager?.requestWhenInUseAuthorization()
        locationManager?.startUpdatingLocation()
        
        awaitClose {
            locationManager?.stopUpdatingLocation()
            locationManager = null
        }
    }

    override fun stopLocationUpdates() {
        locationManager?.stopUpdatingLocation()
        locationManager = null
    }

    companion object {
        private const val MIN_DISTANCE_METERS = 5.0
    }
}
