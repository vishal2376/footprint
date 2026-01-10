package com.nullappstudios.footprint.presentation.map_screen.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.nullappstudios.footprint.domain.model.Location
import com.nullappstudios.footprint.presentation.common.utils.CoordinateUtils
import com.nullappstudios.footprint.presentation.map_screen.MapIds
import com.nullappstudios.footprint.presentation.theme.trackPath
import ovh.plrapps.mapcompose.api.addPath
import ovh.plrapps.mapcompose.api.removePath
import ovh.plrapps.mapcompose.ui.state.MapState

@Composable
fun TrackPathEffect(
	trackPoints: List<Location>,
	mapState: MapState,
) {
	LaunchedEffect(trackPoints) {
		if (trackPoints.size >= 2) {
			mapState.removePath(MapIds.ACTIVE_TRACK_PATH)
			mapState.addPath(
				id = MapIds.ACTIVE_TRACK_PATH,
				color = trackPath,
			) {
				trackPoints.forEach { location ->
					val (x, y) = CoordinateUtils.toNormalized(location.latitude, location.longitude)
					addPoint(x, y)
				}
			}
		}
	}
}
