package com.nullappstudios.footprint.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

object TimeUtils {

	fun now(): Long = Clock.System.now().toEpochMilliseconds()

	fun formatDateTime(timestamp: Long): String {
		val instant = Instant.fromEpochMilliseconds(timestamp)
		val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

		val month = when (localDateTime.month.ordinal + 1) {
			1 -> "Jan"; 2 -> "Feb"; 3 -> "Mar"; 4 -> "Apr"
			5 -> "May"; 6 -> "Jun"; 7 -> "Jul"; 8 -> "Aug"
			9 -> "Sep"; 10 -> "Oct"; 11 -> "Nov"; 12 -> "Dec"
			else -> "???"
		}
		val day = localDateTime.day.toString().padStart(2, '0')
		val hour = localDateTime.hour.toString().padStart(2, '0')
		val minute = localDateTime.minute.toString().padStart(2, '0')

		return "$month $day, $hour:$minute"
	}
}
