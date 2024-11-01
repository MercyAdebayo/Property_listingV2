package com.example.homefinder.utils

import android.content.Context
import android.content.Intent
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import com.example.homefinder.ui.LoginActivity

class TokenAuthenticator(private val appContext: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // Clear the token from SharedPreferences
        val sharedPreferences = appContext.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("TOKEN").apply()

        // Redirect to LoginActivity
        val intent = Intent(appContext, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        appContext.startActivity(intent)

        // Return null to prevent further requests with the invalid token
        return null
    }
}
