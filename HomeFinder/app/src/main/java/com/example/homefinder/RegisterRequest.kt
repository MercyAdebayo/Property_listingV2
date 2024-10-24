package com.example.homefinder

data class RegisterRequest(
    val userName: String,
    val fullName: String,
    val mobile: String,
    val email: String,
    val password: String
)
