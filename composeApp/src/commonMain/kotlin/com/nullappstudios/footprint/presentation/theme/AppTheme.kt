package com.nullappstudios.footprint.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val DarkColorScheme = darkColorScheme(
	primary = primary,
	primaryContainer = primaryContainer,
	onPrimaryContainer = onPrimaryContainer,
	background = background,
	onBackground = onBackground,
	surface = surface,
	onSurface = onSurface,
	surfaceVariant = surfaceVariant,
	onSurfaceVariant = onSurfaceVariant,
	error = error,
	onError = onError,
)

val LightColorScheme = lightColorScheme(
	primary = primary,
	primaryContainer = onPrimaryContainer,
	onPrimaryContainer = primaryContainer,
	background = onBackground,
	onBackground = background,
	surface = onSurface,
	onSurface = surface,
	error = onError,
	onError = error,
)

@Composable
expect fun AppTheme(
	darkTheme: Boolean,
	dynamicColor: Boolean,
	content: @Composable () -> Unit,
)
