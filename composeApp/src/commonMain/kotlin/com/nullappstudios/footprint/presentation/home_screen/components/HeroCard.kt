package com.nullappstudios.footprint.presentation.home_screen.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullappstudios.footprint.presentation.theme.AccentBlue
import com.nullappstudios.footprint.presentation.theme.AccentGreen
import com.nullappstudios.footprint.presentation.theme.AccentPink
import com.nullappstudios.footprint.presentation.theme.GradientHero
import com.nullappstudios.footprint.util.FormatUtils
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val TOTAL_WORLD_TILES = 274_877_906_944L

@Composable
fun HeroCard(
	tilesExplored: Int,
	lastActivityTime: Long?
) {
	val worldPercentage = (tilesExplored.toDouble() / TOTAL_WORLD_TILES * 100)
	val formattedPercentage = if (worldPercentage < 0.0001) {
		"0.0000%"
	} else {
		"${(worldPercentage * 10000).toLong() / 10000.0}%"
	}

	val infiniteTransition = rememberInfiniteTransition(label = "stars")

	val star1Offset by infiniteTransition.animateFloat(
		initialValue = 0f,
		targetValue = 300f,
		animationSpec = infiniteRepeatable(
			animation = tween(8000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "star1"
	)

	val star2Offset by infiniteTransition.animateFloat(
		initialValue = 50f,
		targetValue = 350f,
		animationSpec = infiniteRepeatable(
			animation = tween(12000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "star2"
	)

	val star3Offset by infiniteTransition.animateFloat(
		initialValue = 100f,
		targetValue = 400f,
		animationSpec = infiniteRepeatable(
			animation = tween(10000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "star3"
	)

	val star4Offset by infiniteTransition.animateFloat(
		initialValue = 0f,
		targetValue = 250f,
		animationSpec = infiniteRepeatable(
			animation = tween(6000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "star4"
	)

	Card(
		modifier = Modifier
			.fillMaxWidth()
			.height(180.dp),
		shape = RoundedCornerShape(24.dp),
		colors = CardDefaults.cardColors(containerColor = Color.Transparent)
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Brush.linearGradient(GradientHero))
				.clip(RoundedCornerShape(24.dp))
		) {
			Box(
				modifier = Modifier
					.size(3.dp)
					.offset(x = star1Offset.dp, y = 20.dp)
					.clip(CircleShape)
					.background(Color.White.copy(alpha = 0.7f))
			)
			Box(
				modifier = Modifier
					.size(2.dp)
					.offset(x = star2Offset.dp, y = 60.dp)
					.clip(CircleShape)
					.background(Color.White.copy(alpha = 0.5f))
			)
			Box(
				modifier = Modifier
					.size(4.dp)
					.offset(x = star3Offset.dp, y = 100.dp)
					.clip(CircleShape)
					.background(Color.White.copy(alpha = 0.6f))
			)
			Box(
				modifier = Modifier
					.size(2.dp)
					.offset(x = star4Offset.dp, y = 140.dp)
					.clip(CircleShape)
					.background(Color.White.copy(alpha = 0.4f))
			)

			Box(
				modifier = Modifier
					.size(2.dp)
					.offset(x = 50.dp, y = 30.dp)
					.clip(CircleShape)
					.background(Color.White.copy(alpha = 0.2f))
			)
			Box(
				modifier = Modifier
					.size(1.dp)
					.offset(x = 150.dp, y = 70.dp)
					.clip(CircleShape)
					.background(Color.White.copy(alpha = 0.3f))
			)
			Box(
				modifier = Modifier
					.size(2.dp)
					.offset(x = 250.dp, y = 40.dp)
					.clip(CircleShape)
					.background(Color.White.copy(alpha = 0.25f))
			)

			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(24.dp),
				verticalArrangement = Arrangement.SpaceBetween
			) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.Top
				) {
					Column {
						Text(
							text = "World Explored",
							style = MaterialTheme.typography.titleMedium,
							color = Color.White.copy(alpha = 0.7f)
						)
						AnimatedStatText(
							text = formattedPercentage,
							style = MaterialTheme.typography.displaySmall,
							fontWeight = FontWeight.Bold,
							color = Color.White
						)
					}
					Box(
						modifier = Modifier
							.size(48.dp)
							.clip(CircleShape)
							.background(AccentBlue.copy(alpha = 0.3f)),
						contentAlignment = Alignment.Center
					) {
						Text(
							text = "🌍",
							style = MaterialTheme.typography.headlineMedium
						)
					}
				}

	if (lastActivityTime != null) {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Column {
							Text(
								text = "Last Adventure",
								style = MaterialTheme.typography.labelSmall,
								color = Color.White.copy(alpha = 0.5f)
							)
							Text(
								text = FormatUtils.formatTimeAgo(lastActivityTime),
								style = MaterialTheme.typography.titleSmall,
								fontWeight = FontWeight.SemiBold,
								color = AccentBlue
							)
						}
					}
				} else {
					Column {
						Text(
							text = "$tilesExplored tiles discovered",
							style = MaterialTheme.typography.titleMedium,
							fontWeight = FontWeight.SemiBold,
							color = AccentBlue
						)
						Text(
							text = "Start your first adventure!",
							style = MaterialTheme.typography.bodySmall,
							color = Color.White.copy(alpha = 0.6f)
						)
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun HeroCardPreview() {
	HeroCard(
		tilesExplored = 1250,
		lastActivityTime = null
	)
}
