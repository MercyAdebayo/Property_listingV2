package com.example.homefinder

data class PropertyDetailDto(

    val id: Int,

    val sellRent: Int,

    val name: String,

    val propertyType: String,

    val furnishingType: String,

    val price: Int,

    val bhk: Int,

    val builtArea: Int,

    val carpetArea: Int,

    val city: String,

    val address: String,

    val address2: String,

    val floorNo: Int,

    val totalFloors: Int,

    val mainEntrance: String,

    val security: Int,

    val gated: Boolean,

    val maintenance: Int,

    val age: Int,

    val description: String,

    val photos: List<PhotoDto>

)

