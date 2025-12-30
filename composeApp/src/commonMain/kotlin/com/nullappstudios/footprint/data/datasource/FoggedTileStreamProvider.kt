package com.nullappstudios.footprint.data.datasource

import com.nullappstudios.footprint.domain.model.MapConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.flow.StateFlow
import kotlinx.io.Buffer
import kotlinx.io.RawSource
import ovh.plrapps.mapcompose.core.TileStreamProvider

/**
 * TileStreamProvider that only returns tiles for explored areas.
 * Unexplored tiles return null (blank/no tile).
 */
class FoggedTileStreamProvider(
	private val httpClient: HttpClient,
	private val exploredTilesFlow: StateFlow<Set<String>>,
) : TileStreamProvider {

	override suspend fun getTileStream(row: Int, col: Int, zoomLvl: Int): RawSource? {
		// Check if this tile is explored (avoid string operations)
		if (!isTileExplored(col, row, zoomLvl)) {
			return null
		}

		return try {
			val url = "${MapConfig.OSM_TILE_URL}/$zoomLvl/$col/$row.png"
			val bytes = httpClient.get(url).readRawBytes()
			Buffer().apply { write(bytes) }
		} catch (e: Exception) {
			null
		}
	}

	private fun isTileExplored(col: Int, row: Int, zoomLevel: Int): Boolean {
		val exploredTiles = exploredTilesFlow.value
		val tileKey = "${zoomLevel}_${col}_$row"

		// Direct match
		if (tileKey in exploredTiles) return true

		// Check if parent tiles (lower zoom) are explored
		// Only check a few levels up to avoid too many lookups
		for (z in maxOf(0, zoomLevel - 4) until zoomLevel) {
			val diff = zoomLevel - z
			val parentCol = col shr diff
			val parentRow = row shr diff
			val parentKey = "${z}_${parentCol}_$parentRow"
			if (parentKey in exploredTiles) return true
		}

		return false
	}
}
