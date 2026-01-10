package com.nullappstudios.footprint.presentation.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatCard(
	modifier: Modifier = Modifier,
	icon: ImageVector,
	title: String,
	value: String,
	subtitle: String,
	gradient: List<Color>,
) {
	val gradientBrush = Brush.linearGradient(gradient)

	Box(
		modifier = modifier
			.height(140.dp)
			.clip(RoundedCornerShape(20.dp))
			.background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
			.border(
				width = 1.2.dp,
				brush = gradientBrush,
				shape = RoundedCornerShape(20.dp)
			)
			.padding(16.dp)
	) {
		Column {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Box(
					modifier = Modifier
						.size(36.dp)
						.clip(RoundedCornerShape(10.dp))
						.background(gradientBrush),
					contentAlignment = Alignment.Center
				) {
					Icon(
						imageVector = icon,
						contentDescription = null,
						tint = Color.White,
						modifier = Modifier.size(18.dp)
					)
				}
				Text(
					text = title,
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
				)
			}

			Spacer(modifier = Modifier.weight(1f))

			AnimatedStatText(
				text = value,
				style = MaterialTheme.typography.headlineMedium,
				fontWeight = FontWeight.Bold,
				color = gradient.first()
			)
			Text(
				text = subtitle,
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
			)
		}
	}
}

@Preview
@Composable
fun StatCardPreview() {
	StatCard(
		icon = Icons.Outlined.Route,
		title = "Total Distance",
		value = "12.5km",
		subtitle = "You've traveled",
		gradient = listOf(
			Color(0xFF667eea),
			Color(0xFF764ba2)
		)
	)
}
