package com.nullappstudios.footprint.presentation.permission

import androidx.compose.runtime.Composable

class IOSPermissionHandler : PermissionHandler {
    
    @Composable
    override fun RequestLocationPermission(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit,
        rationaleContent: @Composable (onRequestPermission: () -> Unit) -> Unit
    ) {
        // iOS handles permission dialogs natively via CLLocationManager
        // Permission is requested when startUpdatingLocation() is called
        onPermissionGranted()
    }
}

actual fun createPermissionHandler(): PermissionHandler = IOSPermissionHandler()
