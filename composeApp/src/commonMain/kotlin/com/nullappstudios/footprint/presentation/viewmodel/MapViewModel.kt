package com.nullappstudios.footprint.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullappstudios.footprint.domain.model.Location
import com.nullappstudios.footprint.domain.model.MapConfig
import com.nullappstudios.footprint.domain.usecase.GetLiveLocationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.enableRotation
import ovh.plrapps.mapcompose.api.scale
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.state.MapState
import kotlin.math.pow

data class MapUiState(
    val isTracking: Boolean = false,
    val error: String? = null,
    val currentLocation: Location? = null
)

class MapViewModel(
    private val getLiveLocationUseCase: GetLiveLocationUseCase,
    private val tileStreamProvider: TileStreamProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private var locationJob: Job? = null

    private val worldSize = MapConfig.TILE_SIZE * 2.0.pow(MapConfig.MAX_ZOOM).toInt()
    
    val mapState = MapState(
        levelCount = MapConfig.MAX_ZOOM + 1,
        fullWidth = worldSize,
        fullHeight = worldSize,
        workerCount = 16
    ).apply {
        addLayer(tileStreamProvider)
        enableRotation()
        scale = 0.0001
    }

    fun startTracking() {
        if (locationJob?.isActive == true) return
        
        locationJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isTracking = true, error = null)
            
            getLiveLocationUseCase()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isTracking = false,
                        error = exception.message ?: "Location error"
                    )
                }
                .collect { location ->
                    _uiState.value = _uiState.value.copy(
                        currentLocation = location,
                        error = null
                    )
                }
        }
    }

    fun stopTracking() {
        locationJob?.cancel()
        locationJob = null
        getLiveLocationUseCase.stop()
        _uiState.value = _uiState.value.copy(isTracking = false)
    }

    override fun onCleared() {
        super.onCleared()
        stopTracking()
    }
}
