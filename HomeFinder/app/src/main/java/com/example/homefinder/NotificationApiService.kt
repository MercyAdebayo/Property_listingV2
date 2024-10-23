package com.example.homefinder

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApiService {
    @GET("notifications")
    fun getNotifications(): Call<List<NotificationDto>>
}