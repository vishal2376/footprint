package com.nullappstudios.footprint.presentation.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import footprint.composeapp.generated.resources.Res
import footprint.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

class AndroidPermissionHandler : PermissionHandler {
    
    @Composable
    override fun RequestLocationPermission(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit,
        rationaleContent: @Composable (onRequestPermission: () -> Unit) -> Unit
    ) {
        val context = LocalContext.current
        var showRationale by remember { mutableStateOf(false) }
        var permissionRequested by remember { mutableStateOf(false) }
        
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            
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
    onDismiss: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(Res.drawable.compose_multiplatform),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Location Permission",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "This app needs access to your location to show your position on the map and track your footprints.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Not Now")
                }
                
                Button(
                    onClick = onRequestPermission,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Allow")
                }
            }
        }
    }
}

actual fun createPermissionHandler(): PermissionHandler = AndroidPermissionHandler()
