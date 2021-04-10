package com.example.robustatask.ui.weatherInfo

import android.content.Context
import com.example.robustatask.db.WeatherDao
import com.example.robustatask.model.weatherHistory.WeatherHistoryItem
import com.example.robustatask.model.weatherResponse.CurrentWeatherResponse
import com.example.robustatask.network.RetrofitClient
import com.example.robustatask.shared.apiWrapper.Error
import com.example.robustatask.shared.apiWrapper.GeneralApiResponse
import com.example.robustatask.shared.apiWrapper.Resource
import com.example.robustatask.shared.helper.processCall
import kotlinx.coroutines.delay
import retrofit2.Response

/**
 * Created by Yasser.Elnaggar on 4/9/2021
 * */

class WeatherInfoRepository(private val context: Context, private val weatherDao: WeatherDao) {

    suspend fun addNewWeatherItem(item: WeatherHistoryItem): Long {
        return weatherDao.addWeatherItem(item)
    }

    suspend fun updateWeatherItem(item: WeatherHistoryItem): Boolean {
        val changedRows = weatherDao.updateWeatherItem(item)
        return changedRows >= 1
    }

    suspend fun deleteWeatherItem(item: WeatherHistoryItem): Boolean {
        val deletedRows = weatherDao.deleteWeatherItem(item)
        return deletedRows >= 1
    }


    suspend fun fetchCurrentWeather(latLng: String): Resource<CurrentWeatherResponse> {

        val response = processCall(context) {
            RetrofitClient.weatherServices.currentWeather(latLng) as Response<CurrentWeatherResponse>
        }

        return when (response) {
            is CurrentWeatherResponse -> {
                Resource.Success(response as CurrentWeatherResponse)
            }
            else -> {
                Resource.DataError(response as Error)
            }
        }
    }
}