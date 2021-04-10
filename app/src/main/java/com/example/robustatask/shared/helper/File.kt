package com.example.robustatask.shared.helper

import android.app.Activity
import android.media.ExifInterface
import android.os.Environment
import com.example.robustatask.model.weatherHistory.WeatherHistoryItem
import java.io.File

/**
 *Created by Yasser.Elnaggar on 4/10/2021
 */


fun deleteFile(filePath: String): Boolean {
    var deleted = false

    val file = File(filePath)
    if (file.exists()) {
        deleted = file.delete()
    }
    return deleted
}

fun saveWeatherDataIntoImageFile(item:WeatherHistoryItem){
    item.photoPath?.let {
        val exif = ExifInterface(it)
        exif.setAttribute("weather_condition", item.description)
        exif.setAttribute("weather_temp", item.temp.toString())
        exif.setAttribute("weather_city", item.cityName)
        exif.saveAttributes()
    }

}


fun Activity.getPhotoFile(name: String): File {
    val path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val photoName = "Image-$name.jpg"
    return File(path, photoName)
}