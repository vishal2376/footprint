package com.nullappstudios.footprint.presentation.map_screen.effects

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.nullappstudios.footprint.presentation.map_screen.events.MapEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MapEventsEffect(
	events: Flow<MapEvent>,
	snackbarHostState: SnackbarHostState,
	onNavigateBack: () -> Unit,
	onShowPermissionRequest: () -> Unit,
) {
	LaunchedEffect(Unit) {
		events.collectLatest { event ->
			when (event) {
				MapEvent.NavigateBack -> onNavigateBack()
				MapEvent.RequestLocationPermission -> onShowPermissionRequest()
				is MapEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
			}
		}
	}
}
