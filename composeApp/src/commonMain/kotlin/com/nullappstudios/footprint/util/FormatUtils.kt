package com.nullappstudios.footprint.util

import kotlin.time.Clock

/**
 * Display formatting utilities for distance, duration, and relative time.
 */
object FormatUtils {

	/**
	 * Format distance in meters (e.g., "500m", "2.5km")
	 */
	fun formatDistance(meters: Double): String {
		return when {
			meters < 1000 -> "${meters.toInt()}m"
			else -> {
				val km = meters / 1000
				val formatted = (km * 10).toInt() / 10.0
				"${formatted}km"
			}
		}
	}

	/**
	 * Format duration in seconds (e.g., "5m", "2h 30m")
	 */
	fun formatDuration(seconds: Long): String {
		val hours = seconds / 3600
		val minutes = (seconds % 3600) / 60
		return when {
			hours > 0 -> "${hours}h ${minutes}m"
			minutes > 0 -> "${minutes}m"
			else -> "${seconds}s"
		}
	}

	/**
	 * Format timestamp to relative time (e.g., "2 days ago")
	 */
	fun formatTimeAgo(timestamp: Long): String {
		val now = Clock.System.now().toEpochMilliseconds()
		val diff = now - timestamp
		val days = diff / DAY_MS
		val hours = diff / HOUR_MS
		val minutes = diff / MIN_MS

		return when {
			days > 0 -> "$days days ago"
			hours > 0 -> "$hours hours ago"
			minutes > 0 -> "$minutes min ago"
			else -> "Just now"
		}
	}

	/**
	 * Format number in compact form (e.g., "999", "1k", "1.3k", "10m")
	 */
	fun formatNumber(number: Long): String {
		return when {
			number < 1_000 -> number.toString()
			number < 10_000 -> {
				val k = number / 100 / 10.0
				"${k}k"
			}
			number < 1_000_000 -> "${number / 1_000}k"
			number < 10_000_000 -> {
				val m = number / 100_000 / 10.0
				"${m}m"
			}
			number < 1_000_000_000 -> "${number / 1_000_000}m"
			else -> {
				val b = number / 100_000_000 / 10.0
				"${b}b"
			}
		}
	}

	/**
	 * Format Int number in compact form
	 */
	fun formatNumber(number: Int): String = formatNumber(number.toLong())

	private const val MIN_MS = 60L * 1000
	private const val HOUR_MS = 60L * MIN_MS
	private const val DAY_MS = 24L * HOUR_MS
}
