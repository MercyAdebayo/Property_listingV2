package com.example.homefinder.network

import com.example.homefinder.model.NotificationDto
import retrofit2.Call
import retrofit2.http.GET

interface NotificationApiService {
    @GET("notifications")
    fun getNotifications(): Call<List<NotificationDto>>
}