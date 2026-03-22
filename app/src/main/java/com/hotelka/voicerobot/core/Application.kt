package com.hotelka.voicerobot.core

import android.app.Application
import android.content.Context
import com.hotelka.voicerobot.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application() : Application() {
    override fun onCreate() {
        super.onCreate()
        ContextHolder.applicationContext = applicationContext
        startKoin {
            androidContext(this@Application)
            modules(appModule)
        }
    }
}

object ContextHolder {
    lateinit var applicationContext: Context
}