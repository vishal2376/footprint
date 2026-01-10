package com.nullappstudios.footprint.presentation.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.nullappstudios.footprint.presentation.theme.AccentBlue
import com.nullappstudios.footprint.presentation.theme.AccentCyan

class AndroidPermissionHandler : PermissionHandler {

	@Composable
	override fun RequestLocationPermission(
		onPermissionGranted: () -> Unit,
		onPermissionDenied: () -> Unit,
		rationaleContent: @Composable (onRequestPermission: () -> Unit) -> Unit,
	) {
		val context = LocalContext.current
		var showRationale by remember { mutableStateOf(false) }
		var permissionRequested by remember { mutableStateOf(false) }

		val permissionLauncher = rememberLauncherForActivityResult(
			contract = ActivityResultContracts.RequestMultiplePermissions()
		) { permissions ->
			val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
			val coarseLocationGranted =
				permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

			if (fineLocationGranted || coarseLocationGranted) {
				onPermissionGranted()
			} else {
				onPermissionDenied()
			}
			showRationale = false
		}

		val hasPermission = remember {
			ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.ACCESS_FINE_LOCATION
			) == PackageManager.PERMISSION_GRANTED
		}

		LaunchedEffect(Unit) {
			if (hasPermission) {
				onPermissionGranted()
			} else if (!permissionRequested) {
				showRationale = true
			}
		}

		if (showRationale) {
			Dialog(onDismissRequest = {
				showRationale = false
				onPermissionDenied()
			}) {
				LocationPermissionDialog(
					onRequestPermission = {
						permissionRequested = true
						showRationale = false
						permissionLauncher.launch(
							arrayOf(
								Manifest.permission.ACCESS_FINE_LOCATION,
								Manifest.permission.ACCESS_COARSE_LOCATION
							)
						)
					},
					onDismiss = {
						showRationale = false
						onPermissionDenied()
					}
				)
			}
		}
	}
}

@Composable
private fun LocationPermissionDialog(
	onRequestPermission: () -> Unit,
	onDismiss: () -> Unit,
) {
	val gradientBrush = Brush.linearGradient(listOf(AccentBlue, AccentCyan))

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(20.dp))
			.background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
			.border(
				width = 1.2.dp,
				brush = gradientBrush,
				shape = RoundedCornerShape(20.dp)
			)
			.padding(24.dp)
	) {
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.Start
		) {
			Text(
				text = "Enable Location",
				style = MaterialTheme.typography.headlineSmall,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface
			)

			Spacer(modifier = Modifier.height(16.dp))

			Text(
				text = "Footprint needs location access to track your adventures and show your position on the map.",
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				lineHeight = MaterialTheme.typography.bodyMedium.fontSize.times(1.3)
			)

			Spacer(modifier = Modifier.height(24.dp))

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically
			) {
				TextButton(onClick = onDismiss) {
					Text("Not now")
				}

				Spacer(modifier = Modifier.height(8.dp))

				Button(
					onClick = onRequestPermission,
					shape = RoundedCornerShape(12.dp)
				) {
					Text("Allow")
				}
			}
		}
	}
}

actual fun createPermissionHandler(): PermissionHandler = AndroidPermissionHandler()
