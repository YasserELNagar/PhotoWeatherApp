package com.example.photoweatherapp.ui.weatherInfo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.photoweatherapp.model.weatherHistory.WeatherHistoryItem

/**
 *Created by Yasser.Elnaggar on 4/9/2021
 */
class WeatherInfoViewModelFactory(
    private val application: Application,
    private val repository: WeatherInfoRepository,
    private val weatherItem: WeatherHistoryItem
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherInfoViewModel::class.java)) {
            return WeatherInfoViewModel(application, repository,weatherItem) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}