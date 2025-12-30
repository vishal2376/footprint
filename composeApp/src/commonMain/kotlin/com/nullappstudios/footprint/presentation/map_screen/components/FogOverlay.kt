package com.nullappstudios.footprint.presentation.map_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.nullappstudios.footprint.util.TileKey
import kotlin.math.pow

private val FOG_COLOR = Color(0xFF0A1929)

@Composable
fun FogOverlay(
	exploredTiles: Set<String>,
	currentZoom: Int,
	scrollX: Double,
	scrollY: Double,
	scale: Double,
	mapWidth: Int,
	mapHeight: Int,
	modifier: Modifier = Modifier,
) {
	Canvas(modifier = modifier) {
		// Draw fog background
		drawRect(
			color = FOG_COLOR,
			size = size
		)

		// Cut holes for explored tiles
		exploredTiles.forEach { tileKeyStr ->
			val tileKey = TileKey.fromKey(tileKeyStr)

			// Only draw tiles at or near current zoom level
			if (tileKey.zoom in (currentZoom - 2)..(currentZoom + 2)) {
				drawExploredTileHole(
					tileKey = tileKey,
					scrollX = scrollX,
					scrollY = scrollY,
					scale = scale,
					mapWidth = mapWidth,
					mapHeight = mapHeight
				)
			}
		}
	}
}

private fun DrawScope.drawExploredTileHole(
	tileKey: TileKey,
	scrollX: Double,
	scrollY: Double,
	scale: Double,
	mapWidth: Int,
	mapHeight: Int,
) {
	val tileSize = 256
	val n = 2.0.pow(tileKey.zoom).toInt()

	// Calculate tile position in world coordinates
	val tileWorldX = tileKey.x * tileSize.toDouble()
	val tileWorldY = tileKey.y * tileSize.toDouble()

	// Scale to current view
	val scaledTileSize = tileSize * scale * (2.0.pow(tileKey.zoom) / n)

	// Calculate screen position
	val screenX = (tileWorldX * scale - scrollX).toFloat()
	val screenY = (tileWorldY * scale - scrollY).toFloat()

	// Draw transparent hole (clear blend mode)
	drawRect(
		color = Color.Transparent,
		topLeft = Offset(screenX, screenY),
		size = Size(scaledTileSize.toFloat(), scaledTileSize.toFloat()),
		blendMode = BlendMode.Clear
	)
}
