package com.nullappstudios.footprint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import com.nullappstudios.footprint.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		enableEdgeToEdge()
		super.onCreate(savedInstanceState)

		initKoin {
			androidContext(this@MainActivity)
		}

		setContent {
			App(darkTheme = isSystemInDarkTheme(), dynamicColor = false)
		}
	}
}