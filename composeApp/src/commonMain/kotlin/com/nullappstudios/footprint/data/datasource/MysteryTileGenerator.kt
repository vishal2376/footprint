package com.nullappstudios.footprint.data.datasource

expect fun generateMysteryTile(
	col: Int,
	row: Int,
	zoomLevel: Int,
	gridSize: Int,
): ByteArray
