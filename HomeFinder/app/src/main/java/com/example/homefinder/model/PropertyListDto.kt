package com.example.homefinder.model

data class PropertyListDto(

    val id: Int,

    val sellRent: Int,

    val name: String,

    val propertyType: String,

    val furnishingType: String,

    val price: Int,

    val bhk: Int,

    val builtArea: Int,

    val city: String,

    val country: String,

    val readyToMove: Boolean,

    val estPossessionOn: String,

    val photo: String

)

