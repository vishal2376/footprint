package com.nullappstudios.footprint

import androidx.compose.ui.window.ComposeUIViewController
import com.nullappstudios.footprint.di.initKoin
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

fun MainViewController() = ComposeUIViewController {
	initKoin()
	App(darkTheme = isSystemDarkTheme(), dynamicColor = false)
}

private fun isSystemDarkTheme() =
	UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark