package com.example.homefinder

data class FavoriteDto(
    val id: Int,
    val propertyId: Int,
    val userId: Int,
    val property: PropertyListDto
)

