package com.nullappstudios.footprint.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "tracks",
	indices = [Index(value = ["startTime"])]
)
data class TrackEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,
	val name: String,
	val startTime: Long,
	val endTime: Long? = null,
	val distanceMeters: Double = 0.0,
	val durationSeconds: Long = 0,
)
