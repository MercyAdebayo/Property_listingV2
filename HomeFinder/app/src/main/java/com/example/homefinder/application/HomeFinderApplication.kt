package com.example.homefinder.application

import android.app.Application
import com.example.homefinder.utils.RetrofitInstance

class HomeFinderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize RetrofitInstance with application context
        RetrofitInstance.setup(this)
    }
}
