package com.example.photoweatherapp.model.weatherHistory

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

/**
 *Created by Yasser.Elnaggar on 4/9/2021
 */
@Parcelize
@Entity(tableName = "WeatherItems",primaryKeys = ["id"])
data class WeatherHistoryItem(var id:Long,var temp:Double?,var description:String?,var iconUrl :String?,var cityName:String?,var photoPath:String?):Parcelable{
    constructor():this(0,null,null,null,null,null)
}