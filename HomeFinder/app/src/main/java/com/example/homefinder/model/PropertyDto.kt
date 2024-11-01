package com.example.homefinder.model

import com.example.homefinder.model.PhotoDto


data class PropertyDto(
    val id: Int,
    val sellRent: Int,
    val name: String,
    val propertyTypeId: Int,
    val bhk: Int,
    val furnishingTypeId: Int,
    val price: Int,
    val builtArea: Int,
    val carpetArea: Int,
    val address: String,
    val address2: String?,
    val cityId: Int,
    val cityName: String,
    val country: String,
    val floorNo: Int,
    val totalFloors: Int,
    val readyToMove: Boolean,
    val mainEntrance: String,
    val security: Int,
    val gated: Boolean,
    val maintenance: Int,
    val estPossessionOn: String?,
    val age: Int,
    val description: String,
    val photos: List<PhotoDto>
)
