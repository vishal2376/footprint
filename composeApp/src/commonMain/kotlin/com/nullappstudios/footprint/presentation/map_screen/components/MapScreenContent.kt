package com.nullappstudios.footprint.presentation.map_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nullappstudios.footprint.presentation.common.components.BaseScreen
import com.nullappstudios.footprint.presentation.map_screen.state.MapState
import ovh.plrapps.mapcompose.ui.MapUI

@Composable
fun MapScreenContent(
	state: MapState,
	mapComposeState: ovh.plrapps.mapcompose.ui.state.MapState,
	snackbarHostState: SnackbarHostState,
	onBackClick: () -> Unit,
	onCompassClick: () -> Unit,
	onFollowClick: () -> Unit,
	onTrackingClick: () -> Unit,
) {
	BaseScreen(
		title = "Map",
		showBackButton = true,
		onBackClick = onBackClick,
		snackbarHost = { SnackbarHost(snackbarHostState) },
		floatingActionButton = {
			MapFabColumn(
				state = state,
				onCompassClick = onCompassClick,
				onFollowClick = onFollowClick,
				onTrackingClick = onTrackingClick
			)
		}
	) { paddingValues ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
		) {
			MapUI(
				modifier = Modifier.fillMaxSize(),
				state = mapComposeState
			)

			MapLoadingOverlay(visible = state.currentLocation == null)
		}
	}
}
