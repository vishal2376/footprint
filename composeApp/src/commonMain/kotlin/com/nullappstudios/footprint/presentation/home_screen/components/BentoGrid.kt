package com.nullappstudios.footprint.presentation.home_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nullappstudios.footprint.presentation.home_screen.state.HomeState
import com.nullappstudios.footprint.presentation.theme.GradientDistance
import com.nullappstudios.footprint.presentation.theme.GradientTiles
import com.nullappstudios.footprint.presentation.theme.GradientTime
import com.nullappstudios.footprint.presentation.theme.GradientTracks
import com.nullappstudios.footprint.util.FormatUtils
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BentoGrid(state: HomeState) {
	Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
		HeroCard(
			tilesExplored = state.tilesExplored,
			lastActivityTime = state.lastActivityTime
		)

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(12.dp)
		) {
			StatCard(
				modifier = Modifier.weight(1.5f),
				icon = Icons.Outlined.Route,
				title = "Total Distance",
				value = FormatUtils.formatDistance(state.totalDistance),
				subtitle = "You've traveled",
				gradient = GradientDistance
			)
			StatCard(
				modifier = Modifier.weight(1f),
				icon = Icons.Outlined.GridView,
				title = "Map Tiles",
				value = FormatUtils.formatNumber(state.tilesExplored),
				subtitle = "Unlocked",
				gradient = GradientTiles
			)
		}

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(12.dp)
		) {
			StatCard(
				modifier = Modifier.weight(1f),
				icon = Icons.Outlined.AccessTime,
				title = "Time Spent",
				value = FormatUtils.formatDuration(state.totalDuration),
				subtitle = "Exploring",
				gradient = GradientTime
			)
			StatCard(
				modifier = Modifier.weight(1f),
				icon = Icons.Outlined.Timeline,
				title = "Journeys",
				value = FormatUtils.formatNumber(state.trackCount),
				subtitle = "Completed",
				gradient = GradientTracks
			)
		}
	}
}

@Preview
@Composable
fun BentoGridPreview() {
	BentoGrid(
		state = HomeState(
			totalDistance = 12500.0,
			totalDuration = 9000,
			trackCount = 15,
			tilesExplored = 1250,
			lastActivityTime = null,
			isLoading = false
		)
	)
}
