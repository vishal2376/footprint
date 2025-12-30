package com.nullappstudios.footprint.data.repository

import com.nullappstudios.footprint.data.local.dao.ExploredTileDao
import com.nullappstudios.footprint.data.local.entity.ExploredTileEntity
import com.nullappstudios.footprint.domain.model.ExploredTile
import com.nullappstudios.footprint.domain.repository.ExploredTileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExploredTileRepositoryImpl(
	private val exploredTileDao: ExploredTileDao,
) : ExploredTileRepository {

	override suspend fun markTileExplored(tile: ExploredTile) {
		exploredTileDao.insertTile(tile.toEntity())
	}

	override suspend fun markTilesExplored(tiles: List<ExploredTile>) {
		exploredTileDao.insertTiles(tiles.map { it.toEntity() })
	}

	override fun getExploredTileKeys(): Flow<Set<String>> {
		return exploredTileDao.getAllTileKeys().map { it.toSet() }
	}

	override suspend fun isTileExplored(tileKey: String): Boolean {
		return exploredTileDao.isTileExplored(tileKey)
	}

	override suspend fun getExploredTileCount(): Int {
		return exploredTileDao.getExploredTileCount()
	}

	private fun ExploredTile.toEntity() = ExploredTileEntity(
		tileKey = tileKey,
		x = x,
		y = y,
		zoomLevel = zoom,
		exploredAt = exploredAt
	)
}
