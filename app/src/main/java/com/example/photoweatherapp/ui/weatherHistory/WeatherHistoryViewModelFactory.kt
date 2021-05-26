package com.example.photoweatherapp.ui.weatherHistory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 *Created by Yasser.Elnaggar on 4/10/2021
 */

class WeatherHistoryViewModelFactory(
    private val application: Application,
    private val repository: WeatherHistoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherHistoryViewModel::class.java)) {
            return WeatherHistoryViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}