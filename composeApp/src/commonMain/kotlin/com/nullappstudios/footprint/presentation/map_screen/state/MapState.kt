package com.nullappstudios.footprint.presentation.map_screen.state

import com.nullappstudios.footprint.domain.model.Location

data class MapState(
	val isTracking: Boolean = false,
	val hasPermission: Boolean = false,
	val followLocation: Boolean = true,
	val hasZoomedOnce: Boolean = false,
	val currentLocation: Location? = null,
	val error: String? = null,
)
