package com.example.homefinder

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {

    @POST("auth/login/")

    fun loginUser(@Body loginReqDto: LoginReqDto): Call<LoginResDto>

    @POST("auth/registration/")

    fun signupUser(@Body signupRequest: SignupRequest): Call<SignupResponse>

}

