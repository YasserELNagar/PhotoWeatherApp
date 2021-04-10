package com.example.robustatask.network

import com.example.robustatask.shared.helper.getLanguage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 *Created by Yasser.Elnaggar on 4/9/2021
 */
class RetrofitClient private constructor() {

    companion object{
        private const val BASE_URL = "http://api.weatherapi.com/v1/"
        private const val WEATHER_API_KEY="25e2cab080564db5ad4144841210904"

        private val interceptor: Interceptor = Interceptor { chain ->

            val url = chain.request().url().newBuilder()
                .addQueryParameter("lang", getLanguage())
                .addQueryParameter("key", WEATHER_API_KEY)
                .build()

            val request = chain.request().newBuilder().url(url).build()

            Timber.i(url.toString())


            return@Interceptor chain.proceed(request)
        }


        private val okHttp = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()


        private val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttp)
            .build()



        val weatherServices: WeatherServices by lazy {
            retrofit.create(WeatherServices::class.java)
        }
    }
}