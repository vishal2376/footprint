package com.nullappstudios.footprint.domain.repository

import com.nullappstudios.footprint.domain.model.ExploredTile
import kotlinx.coroutines.flow.Flow

interface ExploredTileRepository {
	suspend fun markTileExplored(tile: ExploredTile)
	suspend fun markTilesExplored(tiles: List<ExploredTile>)
	fun getExploredTileKeys(): Flow<Set<String>>
	suspend fun isTileExplored(tileKey: String): Boolean
	suspend fun getExploredTileCount(): Int
}
