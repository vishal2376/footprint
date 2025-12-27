package com.nullappstudios.footprint.presentation.common.utils

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.tan

/**
 * Converts latitude/longitude to normalized map coordinates (0.0 to 1.0).
 */
object CoordinateUtils {

	fun lonToNormalized(longitude: Double): Double {
		return (longitude + 180.0) / 360.0
	}

	fun latToNormalized(latitude: Double): Double {
		val latRad = latitude * PI / 180.0
		return (1.0 - ln(tan(latRad) + 1.0 / cos(latRad)) / PI) / 2.0
	}

	fun toNormalized(latitude: Double, longitude: Double): Pair<Double, Double> {
		return Pair(lonToNormalized(longitude), latToNormalized(latitude))
	}
}
