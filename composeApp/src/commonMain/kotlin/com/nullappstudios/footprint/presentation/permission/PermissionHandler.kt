package com.nullappstudios.footprint.presentation.permission

import androidx.compose.runtime.Composable

interface PermissionHandler {
	@Composable
	fun RequestLocationPermission(
		onPermissionGranted: () -> Unit,
		onPermissionDenied: () -> Unit,
		rationaleContent: @Composable (onRequestPermission: () -> Unit) -> Unit,
	)
}

expect fun createPermissionHandler(): PermissionHandler
