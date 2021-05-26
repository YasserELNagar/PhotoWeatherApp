package com.example.photoweatherapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.photoweatherapp.model.weatherHistory.WeatherHistoryItem

/**
 *Created by Yasser.Elnaggar on 4/9/2021
 */

@Database(
    entities = [WeatherHistoryItem::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var instance: AppDB? = null
        private val Lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(Lock) {
            instance ?: buildDB(context)
        }

        private fun buildDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDB::class.java, "WeatherDB"
            ).build()

    }
}