package com.nullappstudios.footprint.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullappstudios.footprint.domain.model.Location
import com.nullappstudios.footprint.domain.usecase.GetLocationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationUiState(
    val location: Location? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class LocationViewModel(
    private val getLocationUseCase: GetLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    fun fetchLocation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            getLocationUseCase().fold(
                onSuccess = { location ->
                    _uiState.value = _uiState.value.copy(
                        location = location,
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
