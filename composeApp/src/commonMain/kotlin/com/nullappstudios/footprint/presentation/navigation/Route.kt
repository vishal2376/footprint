package com.nullappstudios.footprint.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
	@Serializable
	data object Home : Route

	@Serializable
	data object Map : Route
}
