package com.example.homefinder.model

data class RegisterRequest(
    val userName: String,
    val fullName: String,
    val mobile: String,
    val email: String,
    val password: String
)
