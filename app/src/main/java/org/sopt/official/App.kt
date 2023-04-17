package org.sopt.official

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.mvrx.mocking.MockableMavericks
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initFlipper()
        initMavericks()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun initMavericks() {
        MockableMavericks.initialize(this)
    }

    private fun initFlipper() {
        FlipperInitializer.init(this)
    }
}
