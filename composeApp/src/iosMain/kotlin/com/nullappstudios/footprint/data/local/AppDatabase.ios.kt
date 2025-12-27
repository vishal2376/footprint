package com.nullappstudios.footprint.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
	val dbFilePath = NSHomeDirectory() + "/Documents/footprint.db"
	return Room.databaseBuilder<AppDatabase>(
		name = dbFilePath
	)
}
