package com.nullappstudios.footprint.domain.repository

import com.nullappstudios.footprint.domain.model.Track
import com.nullappstudios.footprint.domain.model.TrackPoint
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
	suspend fun createTrack(name: String): Long
	suspend fun updateTrack(track: Track)
	suspend fun finishTrack(
		trackId: Long,
		endTime: Long,
		distanceMeters: Double,
		durationSeconds: Long,
	)

	suspend fun addTrackPoint(trackId: Long, point: TrackPoint, orderIndex: Int)
	suspend fun addTrackPoints(trackId: Long, points: List<Pair<TrackPoint, Int>>)
	fun getAllTracks(): Flow<List<Track>>
	suspend fun getTrackById(trackId: Long): Track?
	suspend fun getTrackPoints(trackId: Long): List<TrackPoint>
	fun getTrackPointsFlow(trackId: Long): Flow<List<TrackPoint>>
	suspend fun deleteTrack(trackId: Long)
}
