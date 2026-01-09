package com.nullappstudios.footprint.presentation.home_screen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearOutSlowInEasing

@Composable
fun AnimatedStatText(
    text: String,
    style: TextStyle,
    color: Color,
    fontWeight: FontWeight? = null,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        text.forEach { char ->
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    (slideInVertically(animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)) { -it } + 
                            fadeIn(animationSpec = tween(durationMillis = 800))) togetherWith
                            (slideOutVertically(animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)) { it } + 
                                    fadeOut(animationSpec = tween(durationMillis = 800))) using SizeTransform(clip = false)
                },
                label = "char"
            ) { targetChar ->
                Text(
                    text = targetChar.toString(),
                    style = style,
                    color = color,
                    fontWeight = fontWeight
                )
            }
        }
    }
}
