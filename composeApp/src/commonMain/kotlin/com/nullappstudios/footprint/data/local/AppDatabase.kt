package com.nullappstudios.footprint.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.nullappstudios.footprint.data.local.dao.ExploredTileDao
import com.nullappstudios.footprint.data.local.dao.TrackDao
import com.nullappstudios.footprint.data.local.entity.ExploredTileEntity
import com.nullappstudios.footprint.data.local.entity.TrackEntity
import com.nullappstudios.footprint.data.local.entity.TrackPointEntity

@Database(
	entities = [
		TrackEntity::class,
		TrackPointEntity::class,
		ExploredTileEntity::class
	],
	version = 2,
	exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
	abstract fun trackDao(): TrackDao
	abstract fun exploredTileDao(): ExploredTileDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
	override fun initialize(): AppDatabase
}
