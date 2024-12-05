package com.example.homefinder.network

import com.example.homefinder.model.CityDto
import com.example.homefinder.model.PropertyDto
import com.example.homefinder.model.CommentDto
import com.example.homefinder.model.PropertyListDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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
    fun getCities(): Call<List<CityDto>>


    @GET("api/property/search")
    fun searchProperties(@Query("query") query: String): Call<List<PropertyListDto>>

    @GET("api/property/detail/{id}")
    fun getPropertyDetail(@Path("id") id: Int): Call<PropertyDto>

    // Fetch comments for a specific property
    @GET("api/property/comments/{propertyId}")
    fun getComments(@Path("propertyId") propertyId: Int): Call<List<CommentDto>>

    // Add a comment to a property
    @POST("api/property/add-comment/{propertyId}")
    fun addComment(@Path("propertyId") propertyId: Int, @Body commentDto: CommentDto, @Header("Authorization") token: String): Call<CommentDto>


}