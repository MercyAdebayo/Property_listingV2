package com.example.homefinder

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PropertyApiService {
    // Fetch property list (e.g., 1 = sell, 2 = rent)
    @GET("property/list/{sellRent}")
    fun getPropertyList(@Path("sellRent") sellRent: Int): Call<List<PropertyListDto>>
}

