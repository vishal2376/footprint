package com.nullappstudios.footprint.di

import org.koin.core.context.startKoin
import org.koin.core.KoinApplication

fun initKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModules)
    }
}
