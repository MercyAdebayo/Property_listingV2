package com.example.homefinder

import android.content.Context
import com.example.homefinder.utils.TokenAuthenticator
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:5000/"
    private val gson = GsonBuilder().setLenient().create()

    private lateinit var retrofit: Retrofit

    fun setup(context: Context) {
        val client = OkHttpClient.Builder()
            .authenticator(TokenAuthenticator(context))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val propertyApiService: PropertyApiService by lazy {
        retrofit.create(PropertyApiService::class.java)
    }

    val notificationApiService: NotificationApiService by lazy {
        retrofit.create(NotificationApiService::class.java)
    }
}
