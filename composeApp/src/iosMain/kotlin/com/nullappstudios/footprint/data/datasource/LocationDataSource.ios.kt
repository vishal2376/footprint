package com.nullappstudios.footprint.data.datasource

import com.nullappstudios.footprint.domain.model.Location
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLLocationAccuracyBest
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class IOSLocationDataSource : LocationDataSource {
    
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun getCurrentLocation(): Location {
        return suspendCancellableCoroutine { continuation ->
            val locationManager = CLLocationManager()
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                    val clLocation = didUpdateLocations.lastOrNull() as? CLLocation
                    if (clLocation != null) {
                        clLocation.coordinate.useContents {
                            continuation.resume(
                                Location(
                                    latitude = latitude,
                                    longitude = longitude
                                )
                            )
                        }
                    }
                    manager.stopUpdatingLocation()
                }
                
                override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                    continuation.resumeWithException(Exception(didFailWithError.localizedDescription))
                }
            }
            
            locationManager.delegate = delegate
            locationManager.requestWhenInUseAuthorization()
            locationManager.startUpdatingLocation()
            
            continuation.invokeOnCancellation {
                locationManager.stopUpdatingLocation()
            }
        }
    }
}
