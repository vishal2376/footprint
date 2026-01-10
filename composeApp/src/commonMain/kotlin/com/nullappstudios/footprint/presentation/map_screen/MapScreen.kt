package com.nullappstudios.footprint.presentation.map_screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nullappstudios.footprint.presentation.map_screen.action.MapAction
import com.nullappstudios.footprint.presentation.map_screen.components.MapScreenContent
import com.nullappstudios.footprint.presentation.map_screen.effects.LocationMarkerEffect
import com.nullappstudios.footprint.presentation.map_screen.effects.MapEventsEffect
import com.nullappstudios.footprint.presentation.map_screen.effects.TrackPathEffect
import com.nullappstudios.footprint.presentation.map_screen.viewmodel.MapViewModel
import com.nullappstudios.footprint.presentation.permission.createPermissionHandler
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import ovh.plrapps.mapcompose.api.rotateTo

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

	LaunchedEffect(Unit) {
		viewModel.onAction(MapAction.ToggleTracking)
	}

	MapEventsEffect(
		events = viewModel.events,
		snackbarHostState = snackbarHostState,
		onNavigateBack = onNavigateBack,
		onShowPermissionRequest = { showPermissionRequest = true }
	)

	LocationMarkerEffect(
		currentLocation = state.currentLocation,
		followLocation = state.followLocation,
		hasZoomedOnce = state.hasZoomedOnce,
		mapState = viewModel.mapComposeState,
		onMarkZoomed = viewModel::markZoomed
	)

	TrackPathEffect(
		trackPoints = state.trackPoints,
		mapState = viewModel.mapComposeState
	)

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
		onBackClick = {
			viewModel.onAction(MapAction.ToggleTracking)
			onNavigateBack()
		},
		onCompassClick = {
			scope.launch { viewModel.mapComposeState.rotateTo(0f) }
		},
		onFollowClick = { viewModel.onAction(MapAction.ToggleFollowLocation) },
		onTrackingClick = { viewModel.onAction(MapAction.ToggleTracking) }
	)
}
