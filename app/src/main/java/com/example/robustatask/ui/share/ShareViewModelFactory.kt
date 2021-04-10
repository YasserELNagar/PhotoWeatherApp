package com.example.robustatask.ui.share

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.robustatask.model.weatherHistory.WeatherHistoryItem
import com.example.robustatask.ui.weatherHistory.WeatherHistoryRepository
import com.example.robustatask.ui.weatherHistory.WeatherHistoryViewModel

/**
 *Created by Yasser.Elnaggar on 4/10/2021
 */
class ShareViewModelFactory (
    private val weatherItem:WeatherHistoryItem
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShareViewModel::class.java)) {
            return ShareViewModel(weatherItem) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}