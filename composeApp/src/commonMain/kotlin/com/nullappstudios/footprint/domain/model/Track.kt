package com.nullappstudios.footprint.domain.model

data class Track(
	val id: Long = 0,
	val name: String,
	val startTime: Long,
	val endTime: Long? = null,
	val distanceMeters: Double = 0.0,
	val durationSeconds: Long = 0,
	val points: List<TrackPoint> = emptyList(),
)

data class TrackPoint(
	val latitude: Double,
	val longitude: Double,
	val timestamp: Long,
)
