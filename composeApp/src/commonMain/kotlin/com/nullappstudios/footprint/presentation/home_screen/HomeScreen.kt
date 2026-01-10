package com.nullappstudios.footprint.presentation.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullappstudios.footprint.presentation.home_screen.action.HomeAction
import com.nullappstudios.footprint.presentation.home_screen.components.BentoGrid
import com.nullappstudios.footprint.presentation.home_screen.components.ExploreButton
import com.nullappstudios.footprint.presentation.home_screen.events.HomeEvent
import com.nullappstudios.footprint.presentation.home_screen.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

/**
 * Home screen displaying exploration stats and CTA to start exploring.
 */
@Composable
fun HomeScreen(
	onNavigateToMap: () -> Unit,
	viewModel: HomeViewModel = koinViewModel(),
) {
	val state by viewModel.state.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.events.collectLatest { event ->
			when (event) {
				HomeEvent.NavigateToMap -> onNavigateToMap()
			}
		}
	}

	Scaffold { padding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
				.padding(padding)
				.padding(16.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Column(
				modifier = Modifier
					.weight(1f)
					.verticalScroll(rememberScrollState())
			) {
				Text(
					text = "Footprint",
					style = MaterialTheme.typography.headlineLarge,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.onBackground
				)
				Text(
					text = "Where you go, the world reveals",
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
				)

				Spacer(modifier = Modifier.height(24.dp))

				BentoGrid(state = state)
			}

			ExploreButton(
				onClick = { viewModel.onAction(HomeAction.NavigateToMap) },
				modifier = Modifier.padding(top = 16.dp)
			)
		}
	}
}
