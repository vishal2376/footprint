package com.nullappstudios.footprint.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

object TimeUtils {

	fun now(): Long = Clock.System.now().toEpochMilliseconds()

	fun formatDateTime(timestamp: Long): String {
		val instant = Instant.fromEpochMilliseconds(timestamp)
		val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

		val month = when (localDateTime.monthNumber) {
			1 -> "Jan"; 2 -> "Feb"; 3 -> "Mar"; 4 -> "Apr"
			5 -> "May"; 6 -> "Jun"; 7 -> "Jul"; 8 -> "Aug"
			9 -> "Sep"; 10 -> "Oct"; 11 -> "Nov"; 12 -> "Dec"
			else -> "???"
		}
		val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
		val hour = localDateTime.hour.toString().padStart(2, '0')
		val minute = localDateTime.minute.toString().padStart(2, '0')

		return "$month $day, $hour:$minute"
	}

	fun formatDistance(meters: Double): String {
		return if (meters >= 1000) {
			val km = (meters / 1000 * 100).toLong() / 100.0
			"$km km"
		} else {
			"${meters.toLong()} m"
		}
	}

	fun formatDuration(seconds: Long): String {
		val hours = seconds / 3600
		val minutes = (seconds % 3600) / 60
		val secs = seconds % 60

		return when {
			hours > 0 -> "${hours}h ${minutes}m"
			minutes > 0 -> "${minutes}m ${secs}s"
			else -> "${secs}s"
		}
	}
}
