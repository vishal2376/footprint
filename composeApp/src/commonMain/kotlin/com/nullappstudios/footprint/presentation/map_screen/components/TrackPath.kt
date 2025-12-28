package com.nullappstudios.footprint.presentation.map_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import com.nullappstudios.footprint.domain.model.Location
import com.nullappstudios.footprint.presentation.common.utils.CoordinateUtils
import com.nullappstudios.footprint.presentation.theme.trackPath

@Composable
fun TrackPath(
	points: List<Location>,
	mapWidth: Int,
	mapHeight: Int,
	scale: Double,
	scrollX: Double,
	scrollY: Double,
	modifier: Modifier = Modifier,
	color: Color = trackPath,
	strokeWidth: Float = 6f,
) {
	if (points.size < 2) return

	Canvas(modifier = modifier) {
		val path = Path()
		var isFirst = true

		points.forEach { location ->
			val (normX, normY) = CoordinateUtils.toNormalized(location.latitude, location.longitude)

			// Convert normalized coordinates to screen position
			val screenX = ((normX * mapWidth * scale) - scrollX).toFloat()
			val screenY = ((normY * mapHeight * scale) - scrollY).toFloat()

			if (isFirst) {
				path.moveTo(screenX, screenY)
				isFirst = false
			} else {
				path.lineTo(screenX, screenY)
			}
		}

		drawPath(
			path = path,
			color = color,
			style = Stroke(
				width = strokeWidth,
				cap = StrokeCap.Round,
				join = StrokeJoin.Round
			)
		)
	}
}
