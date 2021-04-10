package com.example.robustatask.model.weatherResponse

data class Current(
    val condition: Condition,
    val is_day: Int,
    val temp_c: Double,
    val temp_f: Double
)