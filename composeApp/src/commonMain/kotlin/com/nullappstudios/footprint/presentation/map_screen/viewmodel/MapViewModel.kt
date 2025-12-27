package com.nullappstudios.footprint.presentation.map_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullappstudios.footprint.domain.model.MapConfig
import com.nullappstudios.footprint.domain.usecase.GetLiveLocationUseCase
import com.nullappstudios.footprint.presentation.map_screen.action.MapAction
import com.nullappstudios.footprint.presentation.map_screen.events.MapEvent
import com.nullappstudios.footprint.presentation.map_screen.state.MapState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.enableRotation
import ovh.plrapps.mapcompose.api.scale
import ovh.plrapps.mapcompose.core.TileStreamProvider
import kotlin.math.pow
import ovh.plrapps.mapcompose.ui.state.MapState as MapComposeState

class MapViewModel(
	private val getLiveLocationUseCase: GetLiveLocationUseCase,
	private val tileStreamProvider: TileStreamProvider,
) : ViewModel() {

	private val _state = MutableStateFlow(MapState())
	val state: StateFlow<MapState> = _state.asStateFlow()

	private val _events = Channel<MapEvent>(Channel.BUFFERED)
	val events = _events.receiveAsFlow()

	private var locationJob: Job? = null

	private val worldSize = MapConfig.TILE_SIZE * 2.0.pow(MapConfig.MAX_ZOOM).toInt()

	val mapComposeState = MapComposeState(
		levelCount = MapConfig.MAX_ZOOM + 1,
		fullWidth = worldSize,
		fullHeight = worldSize,
		workerCount = 16
	).apply {
		addLayer(tileStreamProvider)
		enableRotation()
		scale = 0.0001
	}

	fun onAction(action: MapAction) {
		when (action) {
			MapAction.ToggleTracking -> handleToggleTracking()
			MapAction.ToggleFollowLocation -> handleToggleFollow()
			MapAction.ResetRotation -> { /* Handled in UI via mapComposeState */
			}

			MapAction.RequestPermission -> _events.trySend(MapEvent.RequestLocationPermission)
			MapAction.PermissionGranted -> handlePermissionGranted()
			MapAction.PermissionDenied -> { /* No-op */
			}

			MapAction.NavigateBack -> _events.trySend(MapEvent.NavigateBack)
		}
	}

	fun markZoomed() {
		_state.update { it.copy(hasZoomedOnce = true) }
	}

	private fun handleToggleTracking() {
		if (!_state.value.hasPermission) {
			_events.trySend(MapEvent.RequestLocationPermission)
			return
		}

		if (_state.value.isTracking) {
			stopTracking()
		} else {
			startTracking()
		}
	}

	private fun handleToggleFollow() {
		_state.update { it.copy(followLocation = !it.followLocation) }
	}

	private fun handlePermissionGranted() {
		_state.update { it.copy(hasPermission = true) }
		startTracking()
	}

	private fun startTracking() {
		if (locationJob?.isActive == true) return

		locationJob = viewModelScope.launch {
			_state.update { it.copy(isTracking = true, error = null) }

			getLiveLocationUseCase()
				.catch { exception ->
					_state.update {
						it.copy(isTracking = false, error = exception.message ?: "Location error")
					}
					_events.trySend(MapEvent.ShowSnackbar(exception.message ?: "Location error"))
				}
				.collect { location ->
					_state.update { it.copy(currentLocation = location, error = null) }
				}
		}
	}

	private fun stopTracking() {
		locationJob?.cancel()
		locationJob = null
		getLiveLocationUseCase.stop()
		_state.update { it.copy(isTracking = false) }
	}

	override fun onCleared() {
		super.onCleared()
		stopTracking()
	}
}
