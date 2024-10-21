package com.example.homefinder


import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://localhost5000/"

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

}

