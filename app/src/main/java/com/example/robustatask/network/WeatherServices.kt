package com.example.robustatask.network

import com.example.robustatask.model.weatherResponse.CurrentWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *Created by Yasser.Elnaggar on 4/9/2021
 */
interface WeatherServices {

    @GET("current.json")
    suspend fun currentWeather(
        @Query("q") latLng:String
    ):Response<CurrentWeatherResponse>
}