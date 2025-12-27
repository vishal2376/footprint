package com.nullappstudios.footprint.presentation.map_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nullappstudios.footprint.presentation.common.components.BaseScreen
import com.nullappstudios.footprint.presentation.common.components.LocationMarker
import com.nullappstudios.footprint.presentation.common.utils.CoordinateUtils
import com.nullappstudios.footprint.presentation.map_screen.action.MapAction
import com.nullappstudios.footprint.presentation.map_screen.components.MapFabColumn
import com.nullappstudios.footprint.presentation.map_screen.events.MapEvent
import com.nullappstudios.footprint.presentation.map_screen.state.MapState
import com.nullappstudios.footprint.presentation.map_screen.viewmodel.MapViewModel
import com.nullappstudios.footprint.presentation.permission.createPermissionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import ovh.plrapps.mapcompose.api.addMarker
import ovh.plrapps.mapcompose.api.addPath
import ovh.plrapps.mapcompose.api.centerOnMarker
import ovh.plrapps.mapcompose.api.removeMarker
import ovh.plrapps.mapcompose.api.removePath
import ovh.plrapps.mapcompose.api.rotateTo
import ovh.plrapps.mapcompose.ui.MapUI

@Composable
fun MapScreen(
	onNavigateBack: () -> Unit,
	viewModel: MapViewModel = koinViewModel(),
) {
	val state by viewModel.state.collectAsStateWithLifecycle()
	val snackbarHostState = remember { SnackbarHostState() }
	val permissionHandler = remember { createPermissionHandler() }
	val scope = rememberCoroutineScope()
	var showPermissionRequest by remember { mutableStateOf(false) }

	// Handle one-time events
	LaunchedEffect(Unit) {
		viewModel.events.collectLatest { event ->
			when (event) {
				MapEvent.NavigateBack -> onNavigateBack()
				MapEvent.RequestLocationPermission -> {
					showPermissionRequest = true
				}

				is MapEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
			}
		}
	}

	// Update marker on location change
	LaunchedEffect(state.currentLocation) {
		state.currentLocation?.let { location ->
			val (x, y) = CoordinateUtils.toNormalized(location.latitude, location.longitude)

			viewModel.mapComposeState.removeMarker("user_location")
			viewModel.mapComposeState.addMarker(
				id = "user_location",
				x = x,
				y = y
			) {
				LocationMarker()
			}

			if (state.followLocation) {
				if (!state.hasZoomedOnce) {
					viewModel.mapComposeState.centerOnMarker("user_location", destScale = 1.0)
					viewModel.markZoomed()
				} else {
					viewModel.mapComposeState.centerOnMarker("user_location")
				}
			}
		}
	}

	// Draw track path on map
	LaunchedEffect(state.trackPoints) {
		val points = state.trackPoints
		if (points.size >= 2) {
			// Remove existing path and add updated one
			viewModel.mapComposeState.removePath("active_track")
			viewModel.mapComposeState.addPath(
				id = "active_track",
				color = Color(0xFF1E90FF), // Dodger Blue
			) {
				points.forEach { location ->
					val (x, y) = CoordinateUtils.toNormalized(location.latitude, location.longitude)
					addPoint(x, y)
				}
			}
		}
	}

	// Permission request handler
	if (showPermissionRequest) {
		permissionHandler.RequestLocationPermission(
			onPermissionGranted = {
				showPermissionRequest = false
				viewModel.onAction(MapAction.PermissionGranted)
			},
			onPermissionDenied = {
				showPermissionRequest = false
				viewModel.onAction(MapAction.PermissionDenied)
			},
			rationaleContent = { }
		)
	}

	MapScreenContent(
		state = state,
		mapComposeState = viewModel.mapComposeState,
		snackbarHostState = snackbarHostState,
		onBackClick = onNavigateBack,
		onCompassClick = {
			scope.launch { viewModel.mapComposeState.rotateTo(0f) }
		},
		onFollowClick = { viewModel.onAction(MapAction.ToggleFollowLocation) },
		onTrackingClick = { viewModel.onAction(MapAction.ToggleTracking) }
	)
}

@Composable
private fun MapScreenContent(
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
		}
	}
}
