package com.nullappstudios.footprint.presentation.map_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullappstudios.footprint.presentation.map_screen.state.MapState
import footprint.composeapp.generated.resources.Res
import footprint.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

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
		horizontalAlignment = Alignment.End,
		verticalArrangement = Arrangement.spacedBy(12.dp)
	) {
		SmallFloatingActionButton(onClick = onCompassClick) {
			Text("N", fontWeight = FontWeight.Bold)
		}

		SmallFloatingActionButton(
			onClick = onFollowClick,
			containerColor = if (state.followLocation)
				MaterialTheme.colorScheme.primary
			else
				MaterialTheme.colorScheme.surfaceVariant
		) {
			Text(
				text = if (state.followLocation) "📍" else "🔓",
				fontWeight = FontWeight.Bold
			)
		}

		FloatingActionButton(
			onClick = onTrackingClick,
			containerColor = if (state.isTracking)
				MaterialTheme.colorScheme.error
			else
				FloatingActionButtonDefaults.containerColor
		) {
			Icon(
				painter = painterResource(Res.drawable.compose_multiplatform),
				contentDescription = if (state.isTracking) "Stop Tracking" else "Start Tracking",
				modifier = Modifier.size(24.dp)
			)
		}
	}
}
