package com.example.homefinder

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PropertyApiService {
    // Fetch properties with an optional filters
    @GET("api/property/list")
    fun getPropertyList(@Query("sellRent") sellRent: Int? = null,
                        @Query("cityId") cityId: Int? = null,
                        @Query("sortBy") sortBy: String? = null
    ): Call<List<PropertyListDto>>

    @GET("api/City/cities")
    fun getCities(): Call<List<City>>


    @GET("api/property/search")
    fun searchProperties(@Query("query") query: String): Call<List<PropertyListDto>>

    @GET("api/property/detail/{id}")
    fun getPropertyDetail(@Path("id") id: Int): Call<PropertyDto>

}