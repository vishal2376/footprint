package com.nullappstudios.footprint.domain.model

data class ExploredTile(
	val x: Int,
	val y: Int,
	val zoom: Int,
	val exploredAt: Long,
) {
	val tileKey: String get() = "${zoom}_${x}_$y"
}
