package com.versaiilers.buildmart

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BuildMartApplication : Application() {
    // Hilt будет автоматически управлять зависимостями приложения

}