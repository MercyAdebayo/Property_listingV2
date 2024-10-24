package com.example.homefinder

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:5000/"

    private val gson = GsonBuilder().setLenient().create()

    private val client = OkHttpClient.Builder().build()

    private val retrofit: Retrofit by lazy {

        Retrofit.Builder()

            .baseUrl(BASE_URL)

            .addConverterFactory(GsonConverterFactory.create())

            .build()

    }

    // Provide the AuthService to handle login and registration

    val authService: AuthService by lazy {

        retrofit.create(AuthService::class.java)

    }

    // Provide the PropertyApiService (for property listing, details, etc.)

    val propertyApiService: PropertyApiService by lazy {

        retrofit.create(PropertyApiService::class.java)


    }
    // Provide the Notification ApiService

    val notificationApiService: NotificationApiService by lazy {

        retrofit.create(NotificationApiService::class.java)

    }
}

