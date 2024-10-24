package com.example.homefinder

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {

    @POST("api/Account/login")

    fun loginUser(@Body loginReqDto: LoginReqDto): Call<LoginResDto>

    @POST("/api/Account/register")

    fun signupUser(@Body signupRequest: RegisterRequest): Call<SignupResponse>


}

