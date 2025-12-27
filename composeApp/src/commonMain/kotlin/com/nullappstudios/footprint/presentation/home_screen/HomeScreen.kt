package com.nullappstudios.footprint.presentation.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nullappstudios.footprint.presentation.home_screen.action.HomeAction
import com.nullappstudios.footprint.presentation.home_screen.events.HomeEvent
import com.nullappstudios.footprint.presentation.home_screen.state.HomeState
import com.nullappstudios.footprint.presentation.home_screen.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
	onNavigateToMap: () -> Unit,
	viewModel: HomeViewModel = koinViewModel(),
) {
	val state by viewModel.state.collectAsStateWithLifecycle()

	LaunchedEffect(Unit) {
		viewModel.events.collectLatest { event ->
			when (event) {
				HomeEvent.NavigateToMap -> onNavigateToMap()
			}
		}
	}

	HomeScreenContent(
		state = state,
		onAction = viewModel::onAction
	)
}

@Composable
private fun HomeScreenContent(
	state: HomeState,
	onAction: (HomeAction) -> Unit,
) {
	Scaffold { paddingValues ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues),
			contentAlignment = Alignment.Center
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Text(
					text = state.appName,
					style = MaterialTheme.typography.headlineLarge
				)

				Button(onClick = { onAction(HomeAction.NavigateToMap) }) {
					Text("Open Map")
				}
			}
		}
	}
}
