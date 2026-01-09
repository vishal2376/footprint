package com.nullappstudios.footprint.presentation.map_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nullappstudios.footprint.presentation.common.components.BaseScreen
import com.nullappstudios.footprint.presentation.common.components.LocationMarker
import com.nullappstudios.footprint.presentation.common.utils.CoordinateUtils
import com.nullappstudios.footprint.presentation.map_screen.action.MapAction
import com.nullappstudios.footprint.presentation.map_screen.components.MapFabColumn
import com.nullappstudios.footprint.presentation.map_screen.components.MapLoadingOverlay
import com.nullappstudios.footprint.presentation.map_screen.events.MapEvent
import com.nullappstudios.footprint.presentation.map_screen.state.MapState
import com.nullappstudios.footprint.presentation.map_screen.viewmodel.MapViewModel
import com.nullappstudios.footprint.presentation.permission.createPermissionHandler
import com.nullappstudios.footprint.presentation.theme.trackPath
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

private object MapIds {
	const val USER_LOCATION_MARKER = "user_location"
	const val ACTIVE_TRACK_PATH = "active_track"
}

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

	// Stop and save tracking when leaving screen
	val currentIsTracking = rememberUpdatedState(state.isTracking)
	val currentViewModel = rememberUpdatedState(viewModel)
	DisposableEffect(Unit) {
		onDispose {
			if (currentIsTracking.value) {
				currentViewModel.value.onAction(MapAction.ToggleTracking)
			}
		}
	}

	// Auto-start tracking when screen opens
	LaunchedEffect(Unit) {
		viewModel.onAction(MapAction.ToggleTracking)
	}

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

			viewModel.mapComposeState.removeMarker(MapIds.USER_LOCATION_MARKER)
			viewModel.mapComposeState.addMarker(
				id = MapIds.USER_LOCATION_MARKER,
				x = x,
				y = y
			) {
				LocationMarker()
			}

			if (state.followLocation) {
				if (!state.hasZoomedOnce) {
					viewModel.mapComposeState.centerOnMarker(MapIds.USER_LOCATION_MARKER, destScale = 1.0)
					viewModel.markZoomed()
				} else {
					viewModel.mapComposeState.centerOnMarker(MapIds.USER_LOCATION_MARKER)
				}
			}
		}
	}

	// Draw track path on map
	LaunchedEffect(state.trackPoints) {
		val points = state.trackPoints
		if (points.size >= 2) {
			// Remove existing path and add updated one
			viewModel.mapComposeState.removePath(MapIds.ACTIVE_TRACK_PATH)
			viewModel.mapComposeState.addPath(
				id = MapIds.ACTIVE_TRACK_PATH,
				color = trackPath,
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

			// Loading overlay - shows until location is found
			AnimatedVisibility(
				visible = state.currentLocation == null,
				enter = fadeIn(),
				exit = fadeOut(animationSpec = tween(500))
			) {
				MapLoadingOverlay()
			}
		}
	}
}
