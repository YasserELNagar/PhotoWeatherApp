package com.example.photoweatherapp.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photoweatherapp.model.weatherHistory.WeatherHistoryItem
import timber.log.Timber

/**
 *Created by Yasser.Elnaggar on 4/9/2021
 */
class CameraViewModel : ViewModel() {

    val weatherItem = WeatherHistoryItem()

    private val _takeNewPhoto = MutableLiveData<String?>()
    val takeNewPhoto: LiveData<String?>
        get() = _takeNewPhoto

    private val _navigateToWeatherInfo = MutableLiveData<WeatherHistoryItem?>()
    val navigateToWeatherInfo: LiveData<WeatherHistoryItem?>
        get() = _navigateToWeatherInfo

 private val _navigateToWeatherHistory = MutableLiveData<Boolean>()
    val navigateToWeatherHistory: LiveData<Boolean>
        get() = _navigateToWeatherHistory


    init {
        Timber.i("${this.javaClass.name} created")
    }

    fun onTakePhotoClick() {
        val currentTime = System.currentTimeMillis()
        weatherItem.id = currentTime
        _takeNewPhoto.value = currentTime.toString()
        doneTakingNewPhoto()
    }

    fun onMenuItemClick() {
        _navigateToWeatherHistory.value=true
        doneNavigateToWeatherHistory()
    }



    fun navigateToWeatherInfo(path: String?) {
        weatherItem.photoPath=path
        _navigateToWeatherInfo.value=weatherItem
        doneNavigateToWeatherInfo()
    }

    private fun doneNavigateToWeatherInfo() {
        _navigateToWeatherInfo.value = null
    }

    private fun doneTakingNewPhoto() {
        _takeNewPhoto.value = null
    }
    private fun doneNavigateToWeatherHistory() {
        _navigateToWeatherHistory.value=false
    }

    override fun onCleared() {
        super.onCleared()

        Timber.i("${this.javaClass.name} cleared")

    }
}