package com.nullappstudios.footprint.presentation.map_screen.events

sealed interface MapEvent {
	data object NavigateBack : MapEvent
	data object RequestLocationPermission : MapEvent
	data class ShowSnackbar(val message: String) : MapEvent
}
