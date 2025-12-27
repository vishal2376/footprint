package com.nullappstudios.footprint

import androidx.compose.runtime.Composable
import com.nullappstudios.footprint.presentation.navigation.AppNavigation
import com.nullappstudios.footprint.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(darkTheme: Boolean, dynamicColor: Boolean) {
	AppTheme(darkTheme, dynamicColor) {
		AppNavigation()
	}
}