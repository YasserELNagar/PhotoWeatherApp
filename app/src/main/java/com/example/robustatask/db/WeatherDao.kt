package com.example.robustatask.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.robustatask.model.weatherHistory.WeatherHistoryItem

/**
 *Created by Yasser.Elnaggar on 4/9/2021
 */
@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeatherItem(item: WeatherHistoryItem):Long

    @Update
    suspend fun updateWeatherItem(item: WeatherHistoryItem):Int

    @Delete
    suspend fun deleteWeatherItem(item: WeatherHistoryItem):Int

    @Query("Select * From WeatherItems Order BY id Desc")
    fun getWeatherList(): LiveData<List<WeatherHistoryItem>>

    @Query("Select * From WeatherItems Where id = :itemId")
    suspend fun getWeatherItem(itemId: Int): WeatherHistoryItem?

}