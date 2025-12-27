package com.nullappstudios.footprint.presentation.home_screen.action

sealed interface HomeAction {
	data object NavigateToMap : HomeAction
}
