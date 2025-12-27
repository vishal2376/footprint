package com.nullappstudios.footprint.data.repository

import com.nullappstudios.footprint.data.local.dao.TrackDao
import com.nullappstudios.footprint.data.local.entity.TrackEntity
import com.nullappstudios.footprint.data.local.entity.TrackPointEntity
import com.nullappstudios.footprint.domain.model.Track
import com.nullappstudios.footprint.domain.model.TrackPoint
import com.nullappstudios.footprint.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.nullappstudios.footprint.util.TimeUtils

class TrackRepositoryImpl(
	private val trackDao: TrackDao,
) : TrackRepository {

	override suspend fun createTrack(name: String): Long {
		val entity = TrackEntity(
			name = name,
			startTime = TimeUtils.now()
		)
		return trackDao.insertTrack(entity)
	}

	override suspend fun updateTrack(track: Track) {
		trackDao.updateTrack(track.toEntity())
	}

	override suspend fun finishTrack(
		trackId: Long,
		endTime: Long,
		distanceMeters: Double,
		durationSeconds: Long,
	) {
		val existing = trackDao.getTrackById(trackId) ?: return
		trackDao.updateTrack(
			existing.copy(
				endTime = endTime,
				distanceMeters = distanceMeters,
				durationSeconds = durationSeconds
			)
		)
	}

	override suspend fun addTrackPoint(trackId: Long, point: TrackPoint, orderIndex: Int) {
		trackDao.insertTrackPoint(point.toEntity(trackId, orderIndex))
	}

	override suspend fun addTrackPoints(trackId: Long, points: List<Pair<TrackPoint, Int>>) {
		val entities = points.map { (point, index) -> point.toEntity(trackId, index) }
		trackDao.insertTrackPoints(entities)
	}

	override fun getAllTracks(): Flow<List<Track>> {
		return trackDao.getAllTracks().map { entities ->
			entities.map { it.toDomain() }
		}
	}

	override suspend fun getTrackById(trackId: Long): Track? {
		val entity = trackDao.getTrackById(trackId) ?: return null
		val points = trackDao.getTrackPoints(trackId).map { it.toDomain() }
		return entity.toDomain(points)
	}

	override suspend fun getTrackPoints(trackId: Long): List<TrackPoint> {
		return trackDao.getTrackPoints(trackId).map { it.toDomain() }
	}

	override fun getTrackPointsFlow(trackId: Long): Flow<List<TrackPoint>> {
		return trackDao.getTrackPointsFlow(trackId).map { entities ->
			entities.map { it.toDomain() }
		}
	}

	override suspend fun deleteTrack(trackId: Long) {
		trackDao.deleteTrack(trackId)
	}

	private fun TrackEntity.toDomain(points: List<TrackPoint> = emptyList()) = Track(
		id = id,
		name = name,
		startTime = startTime,
		endTime = endTime,
		distanceMeters = distanceMeters,
		durationSeconds = durationSeconds,
		points = points
	)

	private fun Track.toEntity() = TrackEntity(
		id = id,
		name = name,
		startTime = startTime,
		endTime = endTime,
		distanceMeters = distanceMeters,
		durationSeconds = durationSeconds
	)

	private fun TrackPointEntity.toDomain() = TrackPoint(
		latitude = latitude,
		longitude = longitude,
		timestamp = timestamp
	)

	private fun TrackPoint.toEntity(trackId: Long, orderIndex: Int) = TrackPointEntity(
		trackId = trackId,
		latitude = latitude,
		longitude = longitude,
		timestamp = timestamp,
		orderIndex = orderIndex
	)
}
