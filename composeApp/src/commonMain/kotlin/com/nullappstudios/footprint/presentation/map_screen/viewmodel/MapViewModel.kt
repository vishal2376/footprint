package com.nullappstudios.footprint.presentation.map_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullappstudios.footprint.domain.model.Location
import com.nullappstudios.footprint.domain.model.MapConfig
import com.nullappstudios.footprint.domain.model.TrackPoint
import com.nullappstudios.footprint.domain.repository.TrackRepository
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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import ovh.plrapps.mapcompose.ui.state.MapState as MapComposeState

class MapViewModel(
	private val getLiveLocationUseCase: GetLiveLocationUseCase,
	private val tileStreamProvider: TileStreamProvider,
	private val trackRepository: TrackRepository,
) : ViewModel() {

	private val _state = MutableStateFlow(MapState())
	val state: StateFlow<MapState> = _state.asStateFlow()

	private val _events = Channel<MapEvent>(Channel.BUFFERED)
	val events = _events.receiveAsFlow()

	private var locationJob: Job? = null
	private var trackStartTime: Long = 0
	private var pointIndex = 0

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

		viewModelScope.launch {
			trackStartTime = System.currentTimeMillis()
			pointIndex = 0
			val trackId = trackRepository.createTrack("Track ${formatTime(trackStartTime)}")
			_state.update {
				it.copy(
					isTracking = true,
					activeTrackId = trackId,
					trackPoints = emptyList(),
					error = null
				)
			}

			locationJob = viewModelScope.launch {
				getLiveLocationUseCase()
					.catch { exception ->
						_state.update {
							it.copy(isTracking = false, error = exception.message ?: "Location error")
						}
						_events.trySend(MapEvent.ShowSnackbar(exception.message ?: "Location error"))
					}
					.collect { location ->
						val currentPoints = _state.value.trackPoints
						val newPoints = currentPoints + location

						// Save point to database
						_state.value.activeTrackId?.let { trackId ->
							val trackPoint = TrackPoint(
								latitude = location.latitude,
								longitude = location.longitude,
								timestamp = System.currentTimeMillis()
							)
							trackRepository.addTrackPoint(trackId, trackPoint, pointIndex++)
						}

						_state.update {
							it.copy(
								currentLocation = location,
								trackPoints = newPoints,
								error = null
							)
						}
					}
			}
		}
	}

	private fun stopTracking() {
		locationJob?.cancel()
		locationJob = null
		getLiveLocationUseCase.stop()

		viewModelScope.launch {
			val trackId = _state.value.activeTrackId
			val points = _state.value.trackPoints

			if (trackId != null && points.size >= 2) {
				val endTime = System.currentTimeMillis()
				val distance = calculateTotalDistance(points)
				val duration = (endTime - trackStartTime) / 1000

				trackRepository.finishTrack(
					trackId = trackId,
					endTime = endTime,
					distanceMeters = distance,
					durationSeconds = duration
				)

				_events.trySend(MapEvent.ShowSnackbar("Track saved: ${formatDistance(distance)}"))
			} else if (trackId != null) {
				trackRepository.deleteTrack(trackId)
			}

			_state.update {
				it.copy(
					isTracking = false,
					activeTrackId = null
				)
			}
		}
	}

	private fun calculateTotalDistance(points: List<Location>): Double {
		if (points.size < 2) return 0.0
		return points.zipWithNext().sumOf { (p1, p2) ->
			haversineDistance(p1.latitude, p1.longitude, p2.latitude, p2.longitude)
		}
	}

	private fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
		val R = 6371000.0 // Earth radius in meters
		val dLat = Math.toRadians(lat2 - lat1)
		val dLon = Math.toRadians(lon2 - lon1)
		val a = sin(dLat / 2) * sin(dLat / 2) +
				cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
				sin(dLon / 2) * sin(dLon / 2)
		val c = 2 * atan2(sqrt(a), sqrt(1 - a))
		return R * c
	}

	private fun formatDistance(meters: Double): String {
		return if (meters >= 1000) {
			String.format("%.2f km", meters / 1000)
		} else {
			String.format("%.0f m", meters)
		}
	}

	private fun formatTime(timestamp: Long): String {
		val date = java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault())
		return date.format(java.util.Date(timestamp))
	}

	override fun onCleared() {
		super.onCleared()
		stopTracking()
	}
}
