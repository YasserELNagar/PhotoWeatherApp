package com.example.photoweatherapp.ui.weatherInfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photoweatherapp.model.weatherHistory.WeatherHistoryItem
import com.example.photoweatherapp.model.weatherResponse.CurrentWeatherResponse
import com.example.photoweatherapp.shared.Validations
import com.example.photoweatherapp.shared.apiWrapper.Resource
import com.example.photoweatherapp.shared.helper.deleteFile
import com.example.photoweatherapp.shared.helper.saveWeatherDataIntoImageFile
import kotlinx.coroutines.*
import timber.log.Timber

/**
 *Created by Yasser.Elnaggar on 4/9/2021
 */
class WeatherInfoViewModel(
    application: Application,
    private val repository: WeatherInfoRepository,
    private val weatherItem: WeatherHistoryItem
) : AndroidViewModel(application) {


    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val currentWeatherApiWrapper = MutableLiveData<Resource<CurrentWeatherResponse>>()

    private val _weatherItemData = MutableLiveData<WeatherHistoryItem>()
    val weatherItemData: LiveData<WeatherHistoryItem>
        get() = _weatherItemData


    private val _validateInput = MutableLiveData<Validations?>()
    val validateInput: LiveData<Validations?>
        get() = _validateInput


    private val _goBack = MutableLiveData<Boolean>()
    val goBack: LiveData<Boolean>
        get() = _goBack

    private val _openShareDialog = MutableLiveData<WeatherHistoryItem?>()
    val openShareDialog: LiveData<WeatherHistoryItem?>
        get() = _openShareDialog


    init {
        Timber.i("${this.javaClass.name} created")

        uiScope.launch {
            //set default weather item to be displayed
            _weatherItemData.value = weatherItem

            withContext(Dispatchers.IO) {
                //Save the Default weather Item that contain id and image path and edit it with the new content later
                repository.addNewWeatherItem(weatherItem)
            }
        }

    }

    fun fetchCurrentWeatherData(lat: Double, lng: Double) {
        uiScope.launch {
            Timber.i("request fetchCurrentWeatherData with lat : $lat and lng: $lng")
            currentWeatherApiWrapper.value = Resource.Loading()
            withContext(Dispatchers.IO) {
                val response = repository.fetchCurrentWeather("$lat,$lng")
                currentWeatherApiWrapper.postValue(response)
                Timber.i("fetchCurrentWeatherData response arrived with data : ${response.data.toString()}")

                weatherItem.cityName = response.data?.location?.region
                weatherItem.description = response.data?.current?.condition?.text
                weatherItem.temp = response.data?.current?.temp_c

                _weatherItemData.postValue(weatherItem)

            }
        }

    }

    fun onSaveBtnClick(location: String, temp: String, weatherCondition: String) {
        uiScope.launch {
            when {
                location.isEmpty() -> {
                    _validateInput.value = Validations.LOCATION_EMPTY
                }
                temp.isEmpty() -> {
                    _validateInput.value = Validations.TEMP_EMPTY
                }
                weatherCondition.isEmpty() -> {
                    _validateInput.value = Validations.WEATHER_CONDITION_EMPTY
                }
                else -> {
                    withContext(Dispatchers.IO) {
                        //update weather Item with user entered values
                        repository.updateWeatherItem(weatherItem)
                        //save metadata to image file and open file for sharing
                        saveWeatherDataIntoImageFile(weatherItem)
                        //open sharing dialog
                        _openShareDialog.postValue(weatherItem)
                    }
                }
            }
        }
    }


    fun onCancelClick() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                //delete weather item from db
                repository.deleteWeatherItem(weatherItem)

                //deleted photo file from saved files
                weatherItem.photoPath?.let { deleteFile(it) }

                //finish current fragment
                _goBack.postValue(true)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
        Timber.i("${this.javaClass.name} cleared")
    }
}