package com.example.homefinder.utils

import android.util.Base64
import org.json.JSONObject
import java.util.*

fun isTokenExpired(token: String?): Boolean {
    if (token == null) return true
    try {
        val parts = token.split(".")
        if (parts.size < 3) return true

        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
        val json = JSONObject(payload)

        val exp = json.getLong("exp")
        val expirationDate = Date(exp * 1000)
        return expirationDate.before(Date())
    } catch (e: Exception) {
        e.printStackTrace()
        return true
    }
}