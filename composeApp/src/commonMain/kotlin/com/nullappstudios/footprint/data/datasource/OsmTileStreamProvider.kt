package com.nullappstudios.footprint.data.datasource

import com.nullappstudios.footprint.domain.model.MapConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import kotlinx.io.Buffer
import kotlinx.io.RawSource
import ovh.plrapps.mapcompose.core.TileStreamProvider

class OsmTileStreamProvider(
    private val httpClient: HttpClient
) : TileStreamProvider {
    
    override suspend fun getTileStream(row: Int, col: Int, zoomLvl: Int): RawSource? {
        return try {
            val url = "${MapConfig.OSM_TILE_URL}/$zoomLvl/$col/$row.png"
            val bytes = httpClient.get(url).readRawBytes()
            Buffer().apply { write(bytes) }
        } catch (e: Exception) {
            null
        }
    }
}
