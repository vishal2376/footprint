package com.nullappstudios.footprint.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
	val dbFile = context.getDatabasePath("footprint.db")
	return Room.databaseBuilder<AppDatabase>(
		context = context,
		name = dbFile.absolutePath
	)
}
