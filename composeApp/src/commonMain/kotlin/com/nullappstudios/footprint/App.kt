package com.nullappstudios.footprint

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullappstudios.footprint.presentation.permission.createPermissionHandler
import com.nullappstudios.footprint.presentation.viewmodel.MapViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ovh.plrapps.mapcompose.api.addMarker
import ovh.plrapps.mapcompose.api.centerOnMarker
import ovh.plrapps.mapcompose.api.removeMarker
import ovh.plrapps.mapcompose.api.rotateTo
import ovh.plrapps.mapcompose.ui.MapUI
import footprint.composeapp.generated.resources.Res
import footprint.composeapp.generated.resources.compose_multiplatform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.tan

@Composable
@Preview
fun App() {
    val viewModel: MapViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val permissionHandler = remember { createPermissionHandler() }
    val scope = rememberCoroutineScope()
    
    var hasPermission by remember { mutableStateOf(false) }
    var showPermissionRequest by remember { mutableStateOf(false) }
    var hasZoomedOnce by remember { mutableStateOf(false) }
    
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }
    
    // Update marker position on location change
    LaunchedEffect(uiState.currentLocation) {
        uiState.currentLocation?.let { location ->
            val x = (location.longitude + 180.0) / 360.0
            val latRad = location.latitude * PI / 180.0
            val y = (1.0 - ln(tan(latRad) + 1.0 / cos(latRad)) / PI) / 2.0
            
            // Remove old marker and add new one
            viewModel.mapState.removeMarker("user_location")
            viewModel.mapState.addMarker(
                id = "user_location",
                x = x,
                y = y
            ) {
                LocationMarker()
            }
            
            // Center on marker (zoom to 1.0 only first time)
            if (!hasZoomedOnce) {
                viewModel.mapState.centerOnMarker("user_location", destScale = 1.0)
                hasZoomedOnce = true
            } else {
                viewModel.mapState.centerOnMarker("user_location")
            }
        }
    }
    
    if (showPermissionRequest) {
        permissionHandler.RequestLocationPermission(
            onPermissionGranted = {
                hasPermission = true
                showPermissionRequest = false
                viewModel.startTracking()
            },
            onPermissionDenied = {
                showPermissionRequest = false
            },
            rationaleContent = { }
        )
    }
    
    MaterialTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Compass/North button
                    SmallFloatingActionButton(
                        onClick = {
                            scope.launch {
                                viewModel.mapState.rotateTo(0f)
                                snackbarHostState.showSnackbar("Facing North")
                            }
                        }
                    ) {
                        Text("N", fontWeight = FontWeight.Bold)
                    }
                    
                    // Location tracking button
                    FloatingActionButton(
                        onClick = { 
                            if (!hasPermission) {
                                showPermissionRequest = true
                            } else if (uiState.isTracking) {
                                viewModel.stopTracking()
                            } else {
                                viewModel.startTracking()
                            }
                        },
                        containerColor = if (uiState.isTracking) 
                            MaterialTheme.colorScheme.error 
                        else 
                            FloatingActionButtonDefaults.containerColor
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.compose_multiplatform),
                            contentDescription = if (uiState.isTracking) "Stop Tracking" else "Start Tracking",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                MapUI(
                    modifier = Modifier.fillMaxSize(),
                    state = viewModel.mapState
                )
            }
        }
    }
}

@Composable
private fun LocationMarker() {
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}