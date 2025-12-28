package com.nullappstudios.footprint.presentation.map_screen.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.nullappstudios.footprint.presentation.theme.primary

@Composable
fun MapLoadingOverlay() {
	val infiniteTransition = rememberInfiniteTransition(label = "loading")

	val rotation by infiniteTransition.animateFloat(
		initialValue = 0f,
		targetValue = 360f,
		animationSpec = infiniteRepeatable(
			animation = tween(3000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "rotation"
	)

	val scale by infiniteTransition.animateFloat(
		initialValue = 0.8f,
		targetValue = 1.2f,
		animationSpec = infiniteRepeatable(
			animation = tween(1000, easing = LinearEasing),
			repeatMode = RepeatMode.Reverse
		),
		label = "scale"
	)

	val alpha by infiniteTransition.animateFloat(
		initialValue = 0.4f,
		targetValue = 1f,
		animationSpec = infiniteRepeatable(
			animation = tween(800, easing = LinearEasing),
			repeatMode = RepeatMode.Reverse
		),
		label = "alpha"
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
			Icon(
				imageVector = Icons.Filled.LocationOn,
				contentDescription = "Finding location",
				modifier = Modifier
					.size(72.dp)
					.rotate(rotation)
					.scale(scale)
					.alpha(alpha),
				tint = primary
			)

			Spacer(modifier = Modifier.height(24.dp))

			Text(
				text = "Finding your location...",
				style = MaterialTheme.typography.bodyLarge,
				color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
			)
		}
	}
}
