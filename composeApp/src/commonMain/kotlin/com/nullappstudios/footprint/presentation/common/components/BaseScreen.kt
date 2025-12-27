package com.nullappstudios.footprint.presentation.common.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
	title: String = "",
	showBackButton: Boolean = false,
	onBackClick: (() -> Unit)? = null,
	actions: @Composable RowScope.() -> Unit = {},
	floatingActionButton: @Composable () -> Unit = {},
	snackbarHost: @Composable () -> Unit = {},
	modifier: Modifier = Modifier,
	content: @Composable (PaddingValues) -> Unit,
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			if (title.isNotEmpty() || showBackButton) {
				CenterAlignedTopAppBar(
					title = {
						Text(
							text = title,
							style = MaterialTheme.typography.titleLarge,
							fontWeight = FontWeight.SemiBold,
						)
					},
					navigationIcon = {
						if (showBackButton && onBackClick != null) {
							IconButton(onClick = onBackClick) {
								Icon(
									imageVector = Icons.AutoMirrored.Filled.ArrowBack,
									contentDescription = "Back",
								)
							}
						}
					},
					actions = actions,
					colors = TopAppBarDefaults.topAppBarColors(
						containerColor = MaterialTheme.colorScheme.background,
						titleContentColor = MaterialTheme.colorScheme.onBackground,
						navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
						actionIconContentColor = MaterialTheme.colorScheme.onBackground,
					),
				)
			}
		},
		floatingActionButton = floatingActionButton,
		snackbarHost = snackbarHost,
		containerColor = MaterialTheme.colorScheme.background,
		content = content,
	)
}
