package com.nullappstudios.footprint.presentation.map_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.nullappstudios.footprint.presentation.theme.primary

@Composable
fun MapLoadingOverlay(visible: Boolean) {
	AnimatedVisibility(
		visible = visible,
		enter = fadeIn(),
		exit = fadeOut(animationSpec = tween(500))
	) {
		val infiniteTransition = rememberInfiniteTransition(label = "loading")

		// Radar Ring 1
		val ring1Scale by infiniteTransition.animateFloat(
			initialValue = 0f,
			targetValue = 1f,
			animationSpec = infiniteRepeatable(
				animation = tween(2000, easing = LinearEasing),
				repeatMode = RepeatMode.Restart
			),
			label = "ring1"
		)

		val ring1Alpha by infiniteTransition.animateFloat(
			initialValue = 0.8f,
			targetValue = 0f,
			animationSpec = infiniteRepeatable(
				animation = tween(2000, easing = LinearEasing),
				repeatMode = RepeatMode.Restart
			),
			label = "ring1Alpha"
		)

		// Radar Ring 2 (Offset)
		val ring2Scale by infiniteTransition.animateFloat(
			initialValue = 0f,
			targetValue = 1f,
			animationSpec = infiniteRepeatable(
				animation = tween(2000, delayMillis = 600, easing = LinearEasing),
				repeatMode = RepeatMode.Restart
			),
			label = "ring2"
		)

		val ring2Alpha by infiniteTransition.animateFloat(
			initialValue = 0.8f,
			targetValue = 0f,
			animationSpec = infiniteRepeatable(
				animation = tween(2000, delayMillis = 600, easing = LinearEasing),
				repeatMode = RepeatMode.Restart
			),
			label = "ring2Alpha"
		)

		// Icon Bounce
		val bounceOffset by infiniteTransition.animateFloat(
			initialValue = 0f,
			targetValue = -10f,
			animationSpec = infiniteRepeatable(
				animation = tween(1000, easing = LinearEasing),
				repeatMode = RepeatMode.Reverse
			),
			label = "bounce"
		)

		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background),
			contentAlignment = Alignment.Center
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				// Group Radar and Icon together to ensure concentricity
				Box(
					modifier = Modifier.size(200.dp), // Fixed size to contain the radar rings
					contentAlignment = Alignment.Center
				) {
					// Radar Rings
					Canvas(modifier = Modifier.fillMaxSize()) {
						// Ring 1
						val radius1 = size.minDimension / 2 * ring1Scale
						drawCircle(
							color = primary.copy(alpha = ring1Alpha),
							radius = radius1,
							style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
						)

						// Ring 2
						val radius2 = size.minDimension / 2 * ring2Scale
						drawCircle(
							color = primary.copy(alpha = ring2Alpha),
							radius = radius2,
							style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
						)
					}

					// Icon centered in the radar
					Icon(
						imageVector = Icons.Filled.LocationOn,
						contentDescription = "Finding location",
						modifier = Modifier
							.size(48.dp)
							.offset(y = bounceOffset.dp),
						tint = primary
					)
				}

				Spacer(modifier = Modifier.height(16.dp))

				Text(
					text = "Acquiring Signal...",
					style = MaterialTheme.typography.bodyLarge,
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
				)
			}
		}
	}
}
