package com.nullappstudios.footprint.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nullappstudios.footprint.presentation.common.components.F1TransitionOverlay
import com.nullappstudios.footprint.presentation.home_screen.HomeScreen
import com.nullappstudios.footprint.presentation.map_screen.MapScreen

@Composable
fun AppNavigation(
	navController: NavHostController = rememberNavController(),
) {
	var showTransition by remember { mutableStateOf(false) }
	var pendingNavigation by remember { mutableStateOf<Route?>(null) }

	Box(modifier = Modifier.fillMaxSize()) {
		NavHost(
			navController = navController,
			startDestination = Route.Home
		) {
			composable<Route.Home> {
				HomeScreen(
					onNavigateToMap = {
						showTransition = true
						pendingNavigation = Route.Map
					}
				)
			}

			composable<Route.Map> {
				MapScreen(
					onNavigateBack = {
						navController.popBackStack()
					}
				)
			}
		}

		// F1 Transition Overlay - spans across navigation
		if (showTransition) {
			F1TransitionOverlay(
				modifier = Modifier
					.fillMaxSize()
					.zIndex(100f),
				onMidPoint = {
					pendingNavigation?.let { route ->
						navController.navigate(route)
						pendingNavigation = null
					}
				},
				onComplete = {
					showTransition = false
				}
			)
		}
	}
}
