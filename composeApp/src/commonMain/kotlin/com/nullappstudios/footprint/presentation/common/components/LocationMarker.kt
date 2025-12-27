package com.nullappstudios.footprint.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LocationMarker(
	modifier: Modifier = Modifier,
	outerColor: Color = Color.Blue,
	innerColor: Color = Color.White,
) {
	Box(
		modifier = modifier
			.size(20.dp)
			.clip(CircleShape)
			.background(outerColor),
		contentAlignment = Alignment.Center
	) {
		Box(
			modifier = Modifier
				.size(8.dp)
				.clip(CircleShape)
				.background(innerColor)
		)
	}
}
