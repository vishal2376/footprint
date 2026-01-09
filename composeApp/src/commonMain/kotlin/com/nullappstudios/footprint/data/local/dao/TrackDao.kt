package com.nullappstudios.footprint.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nullappstudios.footprint.data.local.entity.TrackEntity
import com.nullappstudios.footprint.data.local.entity.TrackPointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

	@Insert
	suspend fun insertTrack(track: TrackEntity): Long

	@Update
	suspend fun updateTrack(track: TrackEntity)

	@Insert
	suspend fun insertTrackPoint(point: TrackPointEntity)

	@Insert
	suspend fun insertTrackPoints(points: List<TrackPointEntity>)

	@Query("SELECT * FROM tracks ORDER BY startTime DESC")
	fun getAllTracks(): Flow<List<TrackEntity>>

	@Query("SELECT * FROM tracks WHERE id = :trackId")
	suspend fun getTrackById(trackId: Long): TrackEntity?

	@Query("SELECT * FROM track_points WHERE trackId = :trackId ORDER BY orderIndex ASC")
	suspend fun getTrackPoints(trackId: Long): List<TrackPointEntity>

	@Query("SELECT * FROM track_points WHERE trackId = :trackId ORDER BY orderIndex ASC")
	fun getTrackPointsFlow(trackId: Long): Flow<List<TrackPointEntity>>

	@Query("DELETE FROM tracks WHERE id = :trackId")
	suspend fun deleteTrack(trackId: Long)

	@Query("SELECT COUNT(*) FROM track_points WHERE trackId = :trackId")
	suspend fun getTrackPointCount(trackId: Long): Int

	// Home screen stats
	@Query("SELECT COALESCE(SUM(distanceMeters), 0.0) FROM tracks")
	fun getTotalDistance(): Flow<Double>

	@Query("SELECT COALESCE(SUM(durationSeconds), 0) FROM tracks")
	fun getTotalDuration(): Flow<Long>

	@Query("SELECT COUNT(*) FROM tracks")
	fun getTrackCount(): Flow<Int>

	@Query("SELECT MAX(endTime) FROM tracks")
	fun getLastTrackTime(): Flow<Long?>


}
