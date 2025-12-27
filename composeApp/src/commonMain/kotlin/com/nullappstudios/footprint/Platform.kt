package com.nullappstudios.footprint

interface Platform {
	val name: String
}

expect fun getPlatform(): Platform