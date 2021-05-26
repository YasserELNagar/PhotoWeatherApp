package com.example.photoweatherapp.ui.weatherHistory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photoweatherapp.model.weatherHistory.WeatherHistoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber

/**
 *Created by Yasser.Elnaggar on 4/10/2021
 */
class WeatherHistoryViewModel(application: Application,private val repository: WeatherHistoryRepository):AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _openImageInFullScreen = MutableLiveData<Int?>()
    val openImageInFullScreen: LiveData<Int?>
        get() = _openImageInFullScreen

    private val _openShareDialog = MutableLiveData<WeatherHistoryItem?>()
    val openShareDialog: LiveData<WeatherHistoryItem?>
        get() = _openShareDialog

    val weatherItemsList = repository.fetchWeatherHistoryList()

    init {
        Timber.i("${this.javaClass.name} created")
    }


    fun onWeatherItemClick(position:Int){
        _openImageInFullScreen.value=position
        doneOpenImageInFullScreen()
    }

    fun onShareClick(position: Int) {
        val item = weatherItemsList.value?.get(position)
        _openShareDialog.value=item
        doneOpeningShareDialog()
    }

    private fun doneOpenImageInFullScreen() {
        _openImageInFullScreen.value=null
    }

    private fun doneOpeningShareDialog() {
        _openImageInFullScreen.value=null
    }

    override fun onCleared() {
        super.onCleared()

        Timber.i("${this.javaClass.name} cleared")

    }


}