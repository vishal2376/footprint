package com.nullappstudios.footprint.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullappstudios.footprint.domain.model.Location
import com.nullappstudios.footprint.domain.model.MapConfig
import com.nullappstudios.footprint.domain.usecase.GetLocationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.scale
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.state.MapState
import kotlin.math.pow

data class MapUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentLocation: Location? = null
)

class MapViewModel(
    private val getLocationUseCase: GetLocationUseCase,
    private val tileStreamProvider: TileStreamProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private val worldSize = MapConfig.TILE_SIZE * 2.0.pow(MapConfig.MAX_ZOOM).toInt()
    
    val mapState = MapState(
        levelCount = MapConfig.MAX_ZOOM + 1,
        fullWidth = worldSize,
        fullHeight = worldSize,
        workerCount = 16
    ).apply {
        addLayer(tileStreamProvider)
        scale = 0.0001
    }

    fun fetchLocation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            getLocationUseCase().fold(
                onSuccess = { location ->
                    _uiState.value = _uiState.value.copy(
                        currentLocation = location,
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error"
                    )
                }
            )
        }
    }
}
