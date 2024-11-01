package com.example.homefinder.model

data class FavoriteDto(
    val id: Int,
    val propertyId: Int,
    val userId: Int,
    val property: PropertyListDto
)

