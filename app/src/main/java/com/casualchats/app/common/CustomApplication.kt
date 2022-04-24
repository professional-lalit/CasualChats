package com.casualchats.app.common

import android.app.Application
import android.content.SharedPreferences

class CustomApplication: Application() {

    companion object {
        var instance: CustomApplication? = null
        var sharedPreferences: SharedPreferences? = null
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        sharedPreferences = getSharedPreferences("Casual Chats", MODE_PRIVATE)
    }

}