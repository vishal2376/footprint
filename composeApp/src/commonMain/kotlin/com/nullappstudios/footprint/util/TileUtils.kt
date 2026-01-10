package com.nullappstudios.footprint.util

import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.tan

data class TileKey(
	val x: Int,
	val y: Int,
	val zoom: Int,
) {
	fun toKey(): String = "${zoom}_${x}_$y"

	companion object {
		fun fromKey(key: String): TileKey {
			val parts = key.split("_")
			return TileKey(
				zoom = parts[0].toInt(),
				x = parts[1].toInt(),
				y = parts[2].toInt()
			)
		}
	}
}

object TileUtils {

	private const val TILE_SIZE = 256

	/**
	 * Convert latitude/longitude to tile coordinates at given zoom level.
	 * Uses Web Mercator projection (EPSG:3857).
	 */
	fun latLonToTile(lat: Double, lon: Double, zoom: Int): TileKey {
		val n = 1 shl zoom // 2^zoom
		val x = floor((lon + 180.0) / 360.0 * n).toInt()
		val latRad = lat * PI / 180.0
		val y =
			floor((1.0 - ln(tan(latRad) + 1.0 / kotlin.math.cos(latRad)) / PI) / 2.0 * n).toInt()

		return TileKey(
			x = x.coerceIn(0, n - 1),
			y = y.coerceIn(0, n - 1),
			zoom = zoom
		)
	}

	/**
	 * Convert tile coordinates to lat/lon bounds.
	 * Returns (north, south, west, east).
	 */
	fun tileToBounds(tile: TileKey): TileBounds {
		val n = 1 shl tile.zoom
		val west = tile.x.toDouble() / n * 360.0 - 180.0
		val east = (tile.x + 1).toDouble() / n * 360.0 - 180.0
		val north = tileYToLat(tile.y, tile.zoom)
		val south = tileYToLat(tile.y + 1, tile.zoom)

		return TileBounds(north, south, west, east)
	}

	private fun tileYToLat(y: Int, zoom: Int): Double {
		val n = 1 shl zoom
		val latRad = atan(exp(PI * (1 - 2.0 * y / n)))
		return (2 * latRad - PI / 2) * 180.0 / PI
	}

	/**
	 * Get all tiles that should be explored for a given location.
	 * Returns tiles at multiple zoom levels for better coverage.
	 */
	fun getExplorationTiles(lat: Double, lon: Double, baseZoom: Int = 19): List<TileKey> {
		val tiles = mutableListOf<TileKey>()

		// Explore at base zoom and a few lower zoom levels
		for (z in maxOf(0, baseZoom - 2)..baseZoom) {
			tiles.add(latLonToTile(lat, lon, z))
		}

		return tiles
	}
}

data class TileBounds(
	val north: Double,
	val south: Double,
	val west: Double,
	val east: Double,
)
