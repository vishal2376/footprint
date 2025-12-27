package com.nullappstudios.footprint.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nullappstudios.footprint.presentation.home_screen.HomeScreen
import com.nullappstudios.footprint.presentation.map_screen.MapScreen

@Composable
fun AppNavigation(
	navController: NavHostController = rememberNavController(),
) {
	NavHost(
		navController = navController,
		startDestination = Route.Home
	) {
		composable<Route.Home> {
			HomeScreen(
				onNavigateToMap = {
					navController.navigate(Route.Map)
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
}
