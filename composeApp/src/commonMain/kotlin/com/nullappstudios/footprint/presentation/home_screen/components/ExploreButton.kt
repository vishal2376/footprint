package com.nullappstudios.footprint.presentation.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullappstudios.footprint.presentation.theme.GradientButton
import com.nullappstudios.footprint.presentation.theme.GradientHero
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExploreButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.fillMaxWidth(0.7f)
			.height(56.dp)
			.clip(RoundedCornerShape(16.dp))
			.background(Brush.linearGradient(GradientHero))
			.border(
				width = 1.2.dp,
				brush = Brush.linearGradient(GradientButton),
				shape = RoundedCornerShape(16.dp)
			)
			.clickable(onClick = onClick),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = "Start Exploring",
			style = MaterialTheme.typography.titleMedium,
			fontWeight = FontWeight.Bold,
			color = Color.White
		)
	}
}

@Preview
@Composable
fun ExploreButtonPreview() {
	ExploreButton(onClick = {})
}
