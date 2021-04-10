package com.example.robustatask.model.weatherResponse

data class Location(
    val country: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val region: String
)