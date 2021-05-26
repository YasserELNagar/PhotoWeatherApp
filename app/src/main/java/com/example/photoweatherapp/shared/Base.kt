package com.example.photoweatherapp.shared

import android.app.Application
import com.example.photoweatherapp.shared.helper.Location
import com.facebook.drawee.backends.pipeline.Fresco
import timber.log.Timber

/**
 *Created by Yasser.Elnaggar on 4/9/2021
 */
class Base : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Fresco.initialize(this);
        Location(this)
    }

}