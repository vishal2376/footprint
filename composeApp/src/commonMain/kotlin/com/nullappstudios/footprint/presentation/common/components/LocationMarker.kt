package com.nullappstudios.footprint.presentation.common.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nullappstudios.footprint.presentation.theme.AccentBlue
import com.nullappstudios.footprint.presentation.theme.AccentCyan

@Composable
fun LocationMarker(
	modifier: Modifier = Modifier,
) {
	val infiniteTransition = rememberInfiniteTransition(label = "pulse")

	val pulseScale by infiniteTransition.animateFloat(
		initialValue = 1f,
		targetValue = 2.5f,
		animationSpec = infiniteRepeatable(
			animation = tween(1500, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "pulseScale"
	)

	val pulseAlpha by infiniteTransition.animateFloat(
		initialValue = 0.6f,
		targetValue = 0f,
		animationSpec = infiniteRepeatable(
			animation = tween(1500, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "pulseAlpha"
	)

	Box(
		modifier = modifier.size(48.dp),
		contentAlignment = Alignment.Center
	) {
		Canvas(modifier = Modifier.size(48.dp)) {
			val center = Offset(size.width / 2, size.height / 2)
			val baseRadius = 8.dp.toPx()

			drawCircle(
				color = AccentBlue.copy(alpha = pulseAlpha),
				radius = baseRadius * pulseScale,
				center = center
			)

			drawCircle(
				brush = Brush.radialGradient(
					colors = listOf(AccentCyan, AccentBlue),
					center = center,
					radius = baseRadius
				),
				radius = baseRadius,
				center = center
			)

			drawCircle(
				color = Color.White,
				radius = 3.dp.toPx(),
				center = center
			)
		}
	}
}
