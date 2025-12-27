package com.nullappstudios.footprint.presentation.home_screen.events

sealed interface HomeEvent {
	data object NavigateToMap : HomeEvent
}
