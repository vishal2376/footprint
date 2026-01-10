package com.nullappstudios.footprint.presentation.map_screen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nullappstudios.footprint.presentation.map_screen.state.MapState
import com.nullappstudios.footprint.presentation.theme.trackingStop

@Composable
fun MapFabColumn(
	state: MapState,
	onCompassClick: () -> Unit,
	onFollowClick: () -> Unit,
	onTrackingClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		// Compass FAB
		GlassFab(
			onClick = onCompassClick,
			size = 44.dp,
		) {
			Text(
				text = "N",
				fontWeight = FontWeight.Bold,
				fontSize = 16.sp,
				color = MaterialTheme.colorScheme.primary,
			)
		}

		// Follow Location FAB
		GlassFab(
			onClick = onFollowClick,
			size = 44.dp,
			isActive = state.followLocation,
			activeColor = MaterialTheme.colorScheme.primary,
		) {
			Icon(
				imageVector = if (state.followLocation)
					Icons.Filled.GpsFixed
				else
					Icons.Filled.GpsOff,
				contentDescription = "Follow Location",
				modifier = Modifier.size(22.dp),
				tint = if (state.followLocation)
					Color.White
				else
					MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
			)
		}

		// Main Tracking FAB (larger)
		TrackingFab(
			isTracking = state.isTracking,
			onClick = onTrackingClick,
		)
	}
}

@Composable
private fun GlassFab(
	onClick: () -> Unit,
	size: Dp,
	isActive: Boolean = false,
	activeColor: Color = MaterialTheme.colorScheme.primary,
	content: @Composable () -> Unit,
) {
	val interactionSource = remember { MutableInteractionSource() }
	val isPressed by interactionSource.collectIsPressedAsState()

	val scale by animateFloatAsState(
		targetValue = if (isPressed) 0.9f else 1f,
		animationSpec = spring(stiffness = Spring.StiffnessMedium),
		label = "scale"
	)

	val backgroundColor by animateColorAsState(
		targetValue = if (isActive)
			activeColor
		else
			MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
		label = "bgColor"
	)

	Box(
		modifier = Modifier
			.size(size)
			.scale(scale)
			.shadow(
				elevation = 8.dp,
				shape = CircleShape,
				spotColor = if (isActive) activeColor else Color.Black.copy(alpha = 0.3f)
			)
			.clip(CircleShape)
			.background(backgroundColor)
			.border(
				width = 1.dp,
				color = if (isActive)
					activeColor.copy(alpha = 0.5f)
				else
					Color.White.copy(alpha = 0.1f),
				shape = CircleShape
			)
			.clickable(
				interactionSource = interactionSource,
				indication = null,
				onClick = onClick
			),
		contentAlignment = Alignment.Center
	) {
		content()
	}
}

@Composable
private fun TrackingFab(
	isTracking: Boolean,
	onClick: () -> Unit,
) {
	val interactionSource = remember { MutableInteractionSource() }
	val isPressed by interactionSource.collectIsPressedAsState()

	val scale by animateFloatAsState(
		targetValue = if (isPressed) 0.92f else 1f,
		animationSpec = spring(stiffness = Spring.StiffnessMedium),
		label = "scale"
	)

	val backgroundColor by animateColorAsState(
		targetValue = if (isTracking)
			trackingStop
		else
			MaterialTheme.colorScheme.primary,
		label = "bgColor"
	)

	Box(
		modifier = Modifier
			.size(60.dp)
			.scale(scale)
			.shadow(
				elevation = 12.dp,
				shape = RoundedCornerShape(18.dp),
				spotColor = backgroundColor.copy(alpha = 0.5f)
			)
			.clip(RoundedCornerShape(18.dp))
			.background(
				brush = Brush.verticalGradient(
					colors = listOf(
						backgroundColor,
						backgroundColor.copy(alpha = 0.8f)
					)
				)
			)
			.border(
				width = 1.dp,
				color = Color.White.copy(alpha = 0.2f),
				shape = RoundedCornerShape(18.dp)
			)
			.clickable(
				interactionSource = interactionSource,
				indication = null,
				onClick = onClick
			),
		contentAlignment = Alignment.Center
	) {
		Icon(
			imageVector = Icons.Filled.NearMe,
			contentDescription = if (isTracking) "Stop Tracking" else "Start Tracking",
			modifier = Modifier.size(28.dp),
			tint = Color.White,
		)
	}
}
