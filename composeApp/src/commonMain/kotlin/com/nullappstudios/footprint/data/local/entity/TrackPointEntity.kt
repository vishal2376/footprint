package com.nullappstudios.footprint.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "track_points",
	foreignKeys = [
		ForeignKey(
			entity = TrackEntity::class,
			parentColumns = ["id"],
			childColumns = ["trackId"],
			onDelete = ForeignKey.CASCADE
		)
	],
	indices = [Index("trackId")]
)
data class TrackPointEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,
	val trackId: Long,
	val latitude: Double,
	val longitude: Double,
	val timestamp: Long,
	val orderIndex: Int,
)
