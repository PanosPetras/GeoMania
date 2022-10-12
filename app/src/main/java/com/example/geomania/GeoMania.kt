package com.example.geomania

import android.app.Application
import android.content.Context


class GeoMania : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}