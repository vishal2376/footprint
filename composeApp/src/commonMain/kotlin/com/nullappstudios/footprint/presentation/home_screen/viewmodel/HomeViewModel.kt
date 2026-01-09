package com.nullappstudios.footprint.presentation.home_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullappstudios.footprint.data.local.dao.ExploredTileDao
import com.nullappstudios.footprint.data.local.dao.TrackDao
import com.nullappstudios.footprint.presentation.home_screen.action.HomeAction
import com.nullappstudios.footprint.presentation.home_screen.events.HomeEvent
import com.nullappstudios.footprint.presentation.home_screen.state.HomeState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class HomeViewModel(
	private val trackDao: Lazy<TrackDao>,
	private val exploredTileDao: Lazy<ExploredTileDao>,
) : ViewModel() {

	private val _state = MutableStateFlow(HomeState())
	val state: StateFlow<HomeState> = _state.asStateFlow()

	private val _events = Channel<HomeEvent>()
	val events = _events.receiveAsFlow()

	init {
		viewModelScope.launch(Dispatchers.IO) {
			loadStats()
		}
	}

	fun onAction(action: HomeAction) {
		when (action) {
			HomeAction.NavigateToMap -> {
				viewModelScope.launch {
					_events.send(HomeEvent.NavigateToMap)
				}
			}
		}
	}

	private suspend fun loadStats() {
		val tDao = trackDao.value
		val eDao = exploredTileDao.value
		
		combine(
			tDao.getTotalDistance(),
			tDao.getTotalDuration(),
			tDao.getTrackCount(),
			tDao.getLastTrackTime(),
			eDao.getExploredTileCountFlow()
		) { distance, duration, trackCount, lastTime, tileCount ->
			HomeState(
				totalDistance = distance,
				totalDuration = duration,
				trackCount = trackCount,
				tilesExplored = tileCount,
				lastActivityTime = lastTime,
				isLoading = false
			)
		}
			.onEach { stats -> _state.value = stats }
			.launchIn(viewModelScope)
	}
}
