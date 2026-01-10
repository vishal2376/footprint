package com.nullappstudios.footprint.presentation.map_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullappstudios.footprint.domain.model.ExploredTile
import com.nullappstudios.footprint.domain.model.Location
import com.nullappstudios.footprint.domain.model.MapConfig
import com.nullappstudios.footprint.domain.model.TrackPoint
import com.nullappstudios.footprint.domain.repository.ExploredTileRepository
import com.nullappstudios.footprint.domain.repository.TrackRepository
import com.nullappstudios.footprint.domain.usecase.GetLiveLocationUseCase
import com.nullappstudios.footprint.presentation.map_screen.action.MapAction
import com.nullappstudios.footprint.presentation.map_screen.events.MapEvent
import com.nullappstudios.footprint.presentation.map_screen.state.MapState
import com.nullappstudios.footprint.util.FormatUtils
import com.nullappstudios.footprint.util.TileUtils
import com.nullappstudios.footprint.util.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.enableRotation
import ovh.plrapps.mapcompose.api.reloadTiles
import ovh.plrapps.mapcompose.api.scale
import ovh.plrapps.mapcompose.core.TileStreamProvider
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import ovh.plrapps.mapcompose.ui.state.MapState as MapComposeState

class MapViewModel(
	private val getLiveLocationUseCase: GetLiveLocationUseCase,
	private val tileStreamProvider: TileStreamProvider,
	private val trackRepository: Lazy<TrackRepository>,
	private val exploredTileRepository: Lazy<ExploredTileRepository>,
	private val exploredTilesStateFlow: MutableStateFlow<Set<String>>,
) : ViewModel() {

	private val _state = MutableStateFlow(MapState())
	val state: StateFlow<MapState> = _state.asStateFlow()

	private val _events = Channel<MapEvent>(Channel.BUFFERED)
	val events = _events.receiveAsFlow()

	private var locationJob: Job? = null
	private var trackStartTime: Long = 0
	private var lastExploredTileKey: String? = null
	private var reloadJob: Job? = null
	private val tileMutex = Mutex()

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

	init {
		viewModelScope.launch(Dispatchers.IO) {
			loadExploredTiles()
		}
	}

	private suspend fun loadExploredTiles() {
		exploredTileRepository.value.getExploredTileKeys()
			.onEach { tiles ->
				val previousSize = _state.value.exploredTiles.size
				_state.update { it.copy(exploredTiles = tiles) }
				exploredTilesStateFlow.value = tiles

				// Debounced tile reload - only when new tiles added
				if (tiles.size > previousSize) {
					scheduleReloadTiles()
				}
			}
			.launchIn(viewModelScope)
	}

	private fun scheduleReloadTiles() {
		// Cancel pending reload and schedule new one (debounce 500ms)
		reloadJob?.cancel()
		reloadJob = viewModelScope.launch {
			delay(1000)
			mapComposeState.reloadTiles()
		}
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

		trackStartTime = TimeUtils.now()
		_state.update {
			it.copy(
				isTracking = true,
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

					_state.update {
						it.copy(
							currentLocation = location,
							trackPoints = newPoints,
							error = null
						)
					}

					markTileExploredIfNew(location)
				}
		}
	}

	private fun markTileExploredIfNew(location: Location) {
		val currentTile = TileUtils.latLonToTile(location.latitude, location.longitude, 19)
		val currentKey = currentTile.toKey()

		// Skip if we're still in the same tile
		if (currentKey == lastExploredTileKey) return
		lastExploredTileKey = currentKey

		// Check if tile is already explored (in memory)
		if (currentKey in _state.value.exploredTiles) return

		// Mark as explored
		viewModelScope.launch {
			val tiles = TileUtils.getExplorationTiles(
				lat = location.latitude,
				lon = location.longitude,
				baseZoom = 19
			)

			// Filter out already explored tiles
			val newTiles = tiles.filter { it.toKey() !in _state.value.exploredTiles }
			if (newTiles.isEmpty()) return@launch

			val exploredTiles = newTiles.map { tile ->
				ExploredTile(
					x = tile.x,
					y = tile.y,
					zoom = tile.zoom,
					exploredAt = TimeUtils.now()
				)
			}

			tileMutex.withLock {
				exploredTileRepository.value.markTilesExplored(exploredTiles)
			}
		}
	}

	private fun stopTracking() {
		locationJob?.cancel()
		locationJob = null
		getLiveLocationUseCase.stop()

		val points = _state.value.trackPoints

		if (points.size >= 2) {
			viewModelScope.launch {
				val endTime = TimeUtils.now()
				val distance = calculateTotalDistance(points)
				val duration = (endTime - trackStartTime) / 1000

				val trackName = "Track ${TimeUtils.formatDateTime(trackStartTime)}"
				val trackId = trackRepository.value.createTrack(trackName)

				val trackPoints = points.mapIndexed { index, location ->
					TrackPoint(
						latitude = location.latitude,
						longitude = location.longitude,
						timestamp = trackStartTime + (index * 3000L)
					) to index
				}
				trackRepository.value.addTrackPoints(trackId, trackPoints)

				trackRepository.value.finishTrack(
					trackId = trackId,
					endTime = endTime,
					distanceMeters = distance,
					durationSeconds = duration
				)

				_events.trySend(
					MapEvent.ShowSnackbar(
						"Track saved: ${
							FormatUtils.formatDistance(
								distance
							)
						}"
					)
				)
			}
		}

		_state.update {
			it.copy(
				isTracking = false,
				activeTrackId = null
			)
		}
	}

	private fun calculateTotalDistance(points: List<Location>): Double {
		if (points.size < 2) return 0.0
		return points.zipWithNext().sumOf { (p1, p2) ->
			haversineDistance(p1.latitude, p1.longitude, p2.latitude, p2.longitude)
		}
	}

	private fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
		val R = 6371000.0
		val dLat = kotlin.math.PI / 180 * (lat2 - lat1)
		val dLon = kotlin.math.PI / 180 * (lon2 - lon1)
		val a = sin(dLat / 2) * sin(dLat / 2) +
				cos(kotlin.math.PI / 180 * lat1) * cos(kotlin.math.PI / 180 * lat2) *
				sin(dLon / 2) * sin(dLon / 2)
		val c = 2 * atan2(sqrt(a), sqrt(1 - a))
		return R * c
	}

	override fun onCleared() {
		super.onCleared()
		stopTracking()
	}
}
