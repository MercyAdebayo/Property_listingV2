package com.example.homefinder

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface AuthService {

    @POST("api/Account/login")

    fun loginUser(@Body loginReqDto: LoginReqDto): Call<LoginResDto>

    @POST("/api/Account/register")

    fun signupUser(@Body signupRequest: RegisterRequest): Call<SignupResponse>
    // Fetch user details
    @GET("/api/Account/userinfo")
    fun getUserInfo(@Header("Authorization") token: String ): Call<UserInfoDto>

    @POST("api/account/add-favorite/{propertyId}")
    fun addFavorite(
        @Path("propertyId") propertyId: Int,
        @Header("Authorization") token: String
    ): Call<String>

    @DELETE("api/account/remove-favorite/{propertyId}")
    fun removeFavorite(
        @Path("propertyId") propertyId: Int,
        @Header("Authorization") token: String
    ): Call<String>

    @GET("api/account/is-favorite/{propertyId}")
    fun isFavorite(
        @Path("propertyId") propertyId: Int,
        @Header("Authorization") token: String
    ): Call<Boolean>

    @GET("api/account/favorites")
    fun getUserFavorites(@Header("Authorization") token: String): Call<List<FavoriteDto>>



}

