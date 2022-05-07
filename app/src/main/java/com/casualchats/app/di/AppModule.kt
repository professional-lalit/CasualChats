package com.casualchats.app.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.casualchats.app.common.CustomApplication
import com.casualchats.app.common.Prefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        const val APP_PREF_NAME = "Casual Chats"
    }

    @Provides
    fun context(): Context {
        return CustomApplication.instance?.applicationContext!!
    }

    @Provides
    fun appPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_PREF_NAME, Application.MODE_PRIVATE)
    }

    @Provides
    fun customPrefs(appPrefs: SharedPreferences): Prefs {
        return Prefs(appPrefs)
    }


}