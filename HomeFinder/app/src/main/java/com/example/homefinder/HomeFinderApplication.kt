package com.example.homefinder

import android.app.Application

class HomeFinderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize RetrofitInstance with application context
        RetrofitInstance.setup(this)
    }
}
