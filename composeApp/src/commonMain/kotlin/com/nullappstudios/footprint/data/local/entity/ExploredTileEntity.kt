package com.nullappstudios.footprint.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "explored_tiles")
data class ExploredTileEntity(
	@PrimaryKey
	val tileKey: String, // Format: "z_x_y"
	val x: Int,
	val y: Int,
	val zoomLevel: Int,
	val exploredAt: Long,
)
