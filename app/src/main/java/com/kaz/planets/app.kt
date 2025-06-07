package com.kaz.planets

import android.app.Application
import com.kaz.planets.utils.NetworkMonitor

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkMonitor.init(this)
    }
}
