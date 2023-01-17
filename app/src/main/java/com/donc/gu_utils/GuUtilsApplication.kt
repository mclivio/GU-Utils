package com.donc.gu_utils

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class GuUtilsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            // Timber to save time with logs. https://www.freecodecamp.org/news/how-to-log-more-efficiently-with-timber-a3f41b193940/
        }
    }
    /*Necessary class for Hilt to generate code
    DOC: https://developer.android.com/training/dependency-injection/hilt-android#android-classes */
    companion object {
        lateinit  var appContext: Context
    }
}

