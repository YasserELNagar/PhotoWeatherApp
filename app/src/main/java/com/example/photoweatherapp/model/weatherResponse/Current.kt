package com.example.photoweatherapp.model.weatherResponse

data class Current(
    val condition: Condition,
    val is_day: Int,
    val temp_c: Double,
    val temp_f: Double
)