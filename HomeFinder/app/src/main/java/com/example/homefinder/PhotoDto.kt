package com.example.homefinder

data class PhotoDto(
    val id: Int,
    val imageUrl: String,
    val isPrimary: Boolean,
    val propertyId: Int,
    val publicId: String
)
