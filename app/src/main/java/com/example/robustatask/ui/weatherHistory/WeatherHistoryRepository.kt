package com.example.robustatask.ui.weatherHistory

import androidx.lifecycle.LiveData
import com.example.robustatask.db.WeatherDao
import com.example.robustatask.model.weatherHistory.WeatherHistoryItem

/**
 *Created by Yasser.Elnaggar on 4/10/2021
 */
class WeatherHistoryRepository(private val weatherDao: WeatherDao) {

    fun fetchWeatherHistoryList(): LiveData<List<WeatherHistoryItem>> {
        return weatherDao.getWeatherList()
    }

}