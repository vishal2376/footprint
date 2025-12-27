package com.nullappstudios.footprint.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import footprint.composeapp.generated.resources.Res
import footprint.composeapp.generated.resources.montserrat
import footprint.composeapp.generated.resources.orbitron
import footprint.composeapp.generated.resources.orbitron_bold
import footprint.composeapp.generated.resources.orbitron_medium
import org.jetbrains.compose.resources.Font

@Composable
fun AppTypography(): Typography {
	val montserratFamily = FontFamily(
		Font(Res.font.montserrat, FontWeight.Normal),
	)

	val orbitronFamily = FontFamily(
		Font(Res.font.orbitron, FontWeight.Normal),
		Font(Res.font.orbitron_medium, FontWeight.Medium),
		Font(Res.font.orbitron_bold, FontWeight.Bold),
	)

	return Typography(
		// Display - Orbitron for impact
		displayLarge = TextStyle(
			fontFamily = orbitronFamily,
			fontWeight = FontWeight.Bold,
			fontSize = 57.sp,
			lineHeight = 64.sp,
			letterSpacing = (-0.25).sp,
		),
		displayMedium = TextStyle(
			fontFamily = orbitronFamily,
			fontWeight = FontWeight.Bold,
			fontSize = 45.sp,
			lineHeight = 52.sp,
		),
		displaySmall = TextStyle(
			fontFamily = orbitronFamily,
			fontWeight = FontWeight.Medium,
			fontSize = 36.sp,
			lineHeight = 44.sp,
		),

		// Headline - Orbitron for headers
		headlineLarge = TextStyle(
			fontFamily = orbitronFamily,
			fontWeight = FontWeight.Bold,
			fontSize = 32.sp,
			lineHeight = 40.sp,
		),
		headlineMedium = TextStyle(
			fontFamily = orbitronFamily,
			fontWeight = FontWeight.Medium,
			fontSize = 28.sp,
			lineHeight = 36.sp,
		),
		headlineSmall = TextStyle(
			fontFamily = orbitronFamily,
			fontWeight = FontWeight.Medium,
			fontSize = 24.sp,
			lineHeight = 32.sp,
		),

		// Title - Orbitron for titles
		titleLarge = TextStyle(
			fontFamily = orbitronFamily,
			fontWeight = FontWeight.Medium,
			fontSize = 22.sp,
			lineHeight = 28.sp,
		),
		titleMedium = TextStyle(
			fontFamily = orbitronFamily,
			fontWeight = FontWeight.Medium,
			fontSize = 16.sp,
			lineHeight = 24.sp,
			letterSpacing = 0.15.sp,
		),
		titleSmall = TextStyle(
			fontFamily = orbitronFamily,
			fontWeight = FontWeight.Medium,
			fontSize = 14.sp,
			lineHeight = 20.sp,
			letterSpacing = 0.1.sp,
		),

		// Body - Montserrat for readability
		bodyLarge = TextStyle(
			fontFamily = montserratFamily,
			fontWeight = FontWeight.Normal,
			fontSize = 16.sp,
			lineHeight = 24.sp,
			letterSpacing = 0.5.sp,
		),
		bodyMedium = TextStyle(
			fontFamily = montserratFamily,
			fontWeight = FontWeight.Normal,
			fontSize = 14.sp,
			lineHeight = 20.sp,
			letterSpacing = 0.25.sp,
		),
		bodySmall = TextStyle(
			fontFamily = montserratFamily,
			fontWeight = FontWeight.Normal,
			fontSize = 12.sp,
			lineHeight = 16.sp,
			letterSpacing = 0.4.sp,
		),

		// Label - Montserrat for labels
		labelLarge = TextStyle(
			fontFamily = montserratFamily,
			fontWeight = FontWeight.Medium,
			fontSize = 14.sp,
			lineHeight = 20.sp,
			letterSpacing = 0.1.sp,
		),
		labelMedium = TextStyle(
			fontFamily = montserratFamily,
			fontWeight = FontWeight.Medium,
			fontSize = 12.sp,
			lineHeight = 16.sp,
			letterSpacing = 0.5.sp,
		),
		labelSmall = TextStyle(
			fontFamily = montserratFamily,
			fontWeight = FontWeight.Medium,
			fontSize = 11.sp,
			lineHeight = 16.sp,
			letterSpacing = 0.5.sp,
		),
	)
}
