package com.versaiilers.buildmart

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BuildMartApplication : Application() {
    // Hilt будет автоматически управлять зависимостями приложения
    override fun onCreate() {
        super.onCreate()
        // Инициализация Yandex MapKit
        MapKitFactory.setApiKey("03b19b0c-c370-4e66-b588-08a3fdabbcdc")
        MapKitFactory.initialize(this)
    }

}