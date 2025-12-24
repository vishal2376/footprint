package com.nullappstudios.footprint

import androidx.compose.ui.window.ComposeUIViewController
import com.nullappstudios.footprint.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}