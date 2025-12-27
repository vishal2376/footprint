package com.nullappstudios.footprint

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.nullappstudios.footprint.presentation.navigation.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
	MaterialTheme {
		AppNavigation()
	}
}