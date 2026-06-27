package com.samos.pizza

import android.app.Application
import com.samos.pizza.di.initKoin
import org.koin.android.ext.koin.androidContext

class PizzaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@PizzaApp)
        }
    }
}