package com.nullappstudios.footprint.presentation.common.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

private object F1Colors {
	val Red = Color(0xFFFF3B30)
	val Yellow = Color(0xFFFFCC00)
	val Green = Color(0xFF34C759)
}

@Composable
fun F1TransitionOverlay(
	modifier: Modifier = Modifier,
	onMidPoint: () -> Unit,
	onComplete: () -> Unit,
) {
	val bar1Progress = remember { Animatable(0f) }
	val bar2Progress = remember { Animatable(0f) }
	val bar3Progress = remember { Animatable(0f) }

	LaunchedEffect(Unit) {
		// Stage 1: Bars slide in from bottom (staggered)
		bar1Progress.animateTo(1f, tween(250, easing = FastOutSlowInEasing))
		delay(80)
		bar2Progress.animateTo(1f, tween(250, easing = FastOutSlowInEasing))
		delay(80)
		bar3Progress.animateTo(1f, tween(250, easing = FastOutSlowInEasing))

		// Navigate at midpoint while bars are visible
		delay(200)
		onMidPoint()

		// Hold briefly for screen to load
		delay(300)

		// Stage 2: Bars slide out together (GO!)
		bar1Progress.animateTo(0f, tween(180, easing = FastOutSlowInEasing))
		bar2Progress.animateTo(0f, tween(180, easing = FastOutSlowInEasing))
		bar3Progress.animateTo(0f, tween(180, easing = FastOutSlowInEasing))

		onComplete()
	}

	Box(modifier = modifier) {
		Row(
			modifier = Modifier.fillMaxSize(),
			verticalAlignment = Alignment.Bottom
		) {
			Box(
				modifier = Modifier
					.weight(1f)
					.fillMaxHeight(bar1Progress.value)
					.background(F1Colors.Red)
			)
			Box(
				modifier = Modifier
					.weight(1f)
					.fillMaxHeight(bar2Progress.value)
					.background(F1Colors.Yellow)
			)
			Box(
				modifier = Modifier
					.weight(1f)
					.fillMaxHeight(bar3Progress.value)
					.background(F1Colors.Green)
			)
		}
	}
}
