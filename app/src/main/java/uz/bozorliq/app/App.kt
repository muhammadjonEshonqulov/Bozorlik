package uz.bozorliq.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext

        MobileAds.initialize(this) { }

    }

    companion object {
        lateinit var context: Context
    }
}