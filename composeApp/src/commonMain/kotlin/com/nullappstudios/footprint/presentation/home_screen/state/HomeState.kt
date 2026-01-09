package com.nullappstudios.footprint.presentation.home_screen.state

data class HomeState(
	val totalDistance: Double = 0.0,
	val totalDuration: Long = 0,
	val trackCount: Int = 0,
	val tilesExplored: Int = 0,
	val lastActivityTime: Long? = null,
	val isLoading: Boolean = true,
)
