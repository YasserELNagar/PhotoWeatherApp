package com.example.photoweatherapp.shared.apiWrapper

/**
 * Created by Yasser.Elnaggar on 4/9/2021
 * */

class GeneralApiResponse<T>(
    val data:T,
    val error: Error
)