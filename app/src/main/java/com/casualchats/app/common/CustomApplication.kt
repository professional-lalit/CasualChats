package com.casualchats.app.common

import android.app.Application
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CustomApplication : Application() {

    companion object {
        var instance: CustomApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}