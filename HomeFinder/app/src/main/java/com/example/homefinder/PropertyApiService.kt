package com.example.homefinder

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PropertyApiService {
    // Fetch property list (e.g., 1 = sell, 2 = rent)
    @GET("/api/property/list/{sellRent}")
    fun getPropertyList(@Path("sellRent") sellRent: Int): Call<List<PropertyListDto>>
    @GET("property/search")
    fun searchProperties(@Query("query") query: String): Call<List<PropertyListDto>>
}

