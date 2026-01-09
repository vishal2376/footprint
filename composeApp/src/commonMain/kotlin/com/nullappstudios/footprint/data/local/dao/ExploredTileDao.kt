package com.nullappstudios.footprint.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nullappstudios.footprint.data.local.entity.ExploredTileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExploredTileDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertTile(tile: ExploredTileEntity)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertTiles(tiles: List<ExploredTileEntity>)

	@Query("SELECT * FROM explored_tiles WHERE zoomLevel = :zoom")
	fun getTilesByZoom(zoom: Int): Flow<List<ExploredTileEntity>>

	@Query("SELECT * FROM explored_tiles")
	fun getAllTiles(): Flow<List<ExploredTileEntity>>

	@Query("SELECT tileKey FROM explored_tiles")
	fun getAllTileKeys(): Flow<List<String>>

	@Query("SELECT EXISTS(SELECT 1 FROM explored_tiles WHERE tileKey = :tileKey)")
	suspend fun isTileExplored(tileKey: String): Boolean

	@Query("SELECT COUNT(*) FROM explored_tiles")
	suspend fun getExploredTileCount(): Int

	@Query("SELECT COUNT(*) FROM explored_tiles")
	fun getExploredTileCountFlow(): Flow<Int>
}
