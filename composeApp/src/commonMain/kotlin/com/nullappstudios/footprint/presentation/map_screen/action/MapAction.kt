package com.nullappstudios.footprint.presentation.map_screen.action

sealed interface MapAction {
	data object ToggleTracking : MapAction
	data object ToggleFollowLocation : MapAction
	data object ResetRotation : MapAction
	data object RequestPermission : MapAction
	data object PermissionGranted : MapAction
	data object PermissionDenied : MapAction
	data object NavigateBack : MapAction
}
