package com.nullappstudios.footprint.presentation.map_screen.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.nullappstudios.footprint.presentation.common.utils.CoordinateUtils
import com.nullappstudios.footprint.presentation.map_screen.MapIds
import com.nullappstudios.footprint.domain.model.Location
import ovh.plrapps.mapcompose.api.addMarker
import ovh.plrapps.mapcompose.api.centerOnMarker
import ovh.plrapps.mapcompose.api.removeMarker
import ovh.plrapps.mapcompose.ui.state.MapState
import com.nullappstudios.footprint.presentation.common.components.LocationMarker

@Composable
fun LocationMarkerEffect(
	currentLocation: Location?,
	followLocation: Boolean,
	hasZoomedOnce: Boolean,
	mapState: MapState,
	onMarkZoomed: () -> Unit
) {
	LaunchedEffect(currentLocation) {
		currentLocation?.let { location ->
			val (x, y) = CoordinateUtils.toNormalized(location.latitude, location.longitude)

			mapState.removeMarker(MapIds.USER_LOCATION_MARKER)
			mapState.addMarker(
				id = MapIds.USER_LOCATION_MARKER,
				x = x,
				y = y
			) {
				LocationMarker()
			}

			if (followLocation) {
				if (!hasZoomedOnce) {
					mapState.centerOnMarker(MapIds.USER_LOCATION_MARKER, destScale = 1.0)
					onMarkZoomed()
				} else {
					mapState.centerOnMarker(MapIds.USER_LOCATION_MARKER)
				}
			}
		}
	}
}
