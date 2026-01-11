package com.nullappstudios.footprint.data.datasource

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt
import java.io.ByteArrayOutputStream

actual fun generateMysteryTile(
	col: Int,
	row: Int,
	zoomLevel: Int,
	gridSize: Int,
): ByteArray {
	val tileSize = 256

	val bitmap = createBitmap(tileSize, tileSize)
	val canvas = Canvas(bitmap)

	// Dark background
	canvas.drawColor("#000408".toColorInt())

	// Calculate position in grid
	val gridX = col % gridSize
	val gridY = row % gridSize

	// Draw border only on edges of the chunk
	val borderPaint = Paint().apply {
		color = "#181823".toColorInt()
		style = Paint.Style.STROKE
		strokeWidth = 5f
		isAntiAlias = true
	}

	// Top border
	if (gridY == 0) {
		canvas.drawLine(0f, 1f, tileSize.toFloat(), 1f, borderPaint)
	}
	// Bottom border
	if (gridY == gridSize - 1) {
		canvas.drawLine(
			0f,
			(tileSize - 1).toFloat(),
			tileSize.toFloat(),
			(tileSize - 1).toFloat(),
			borderPaint
		)
	}
	// Left border
	if (gridX == 0) {
		canvas.drawLine(1f, 0f, 1f, tileSize.toFloat(), borderPaint)
	}
	// Right border
	if (gridX == gridSize - 1) {
		canvas.drawLine(
			(tileSize - 1).toFloat(),
			0f,
			(tileSize - 1).toFloat(),
			tileSize.toFloat(),
			borderPaint
		)
	}

	// Draw question mark only in center tiles
	val centerStart = gridSize / 2 - if (gridSize % 2 == 0) 1 else 0
	val centerEnd = gridSize / 2

	if (gridX in centerStart..centerEnd && gridY in centerStart..centerEnd) {
		// Scale question mark size based on zoom level
		val zoomScale = when {
			zoomLevel >= 12 -> 0.6f
			zoomLevel >= 9 -> 1.0f
			else -> 1.3f
		}

		val textPaint = Paint().apply {
			color = "#3A3A3D".toColorInt()
			textSize = 80f * gridSize * zoomScale
			textAlign = Paint.Align.CENTER
			isAntiAlias = true
		}

		// Calculate offset based on position in grid
		val gridCenter = (gridSize - 1) / 2f
		val centerOffsetX = tileSize / 2f + (gridCenter - gridX) * tileSize
		val centerOffsetY =
			tileSize / 2f + (gridCenter - gridY) * tileSize + 25f * gridSize * zoomScale

		canvas.drawText("?", centerOffsetX, centerOffsetY, textPaint)
	}

	// Convert to PNG bytes
	val outputStream = ByteArrayOutputStream()
	bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
	bitmap.recycle()
	return outputStream.toByteArray()
}
