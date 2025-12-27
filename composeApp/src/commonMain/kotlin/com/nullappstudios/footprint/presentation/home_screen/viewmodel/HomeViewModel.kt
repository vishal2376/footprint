package com.nullappstudios.footprint.presentation.home_screen.viewmodel

import androidx.lifecycle.ViewModel
import com.nullappstudios.footprint.presentation.home_screen.action.HomeAction
import com.nullappstudios.footprint.presentation.home_screen.events.HomeEvent
import com.nullappstudios.footprint.presentation.home_screen.state.HomeState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class HomeViewModel : ViewModel() {

	private val _state = MutableStateFlow(HomeState())
	val state: StateFlow<HomeState> = _state.asStateFlow()

	private val _events = Channel<HomeEvent>(Channel.BUFFERED)
	val events = _events.receiveAsFlow()

	fun onAction(action: HomeAction) {
		when (action) {
			HomeAction.NavigateToMap -> {
				_events.trySend(HomeEvent.NavigateToMap)
			}
		}
	}
}
