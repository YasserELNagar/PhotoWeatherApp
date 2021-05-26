package com.example.photoweatherapp.ui.weatherHistory

import androidx.lifecycle.LiveData
import com.example.photoweatherapp.db.WeatherDao
import com.example.photoweatherapp.model.weatherHistory.WeatherHistoryItem

/**
 *Created by Yasser.Elnaggar on 4/10/2021
 */
class WeatherHistoryRepository(private val weatherDao: WeatherDao) {

    fun fetchWeatherHistoryList(): LiveData<List<WeatherHistoryItem>> {
        return weatherDao.getWeatherList()
    }

}