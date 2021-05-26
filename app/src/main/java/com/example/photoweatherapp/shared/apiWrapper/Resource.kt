package com.example.photoweatherapp.shared.apiWrapper

/**
 * Created by Yasser.Elnaggar on 4/9/2021
 * */
sealed class Resource<T>(
    val data: T? = null,
    val error: Error? = null,
    val status: Status = Status.None
) {

class Success<T>(data: T) : Resource<T>(status = Status.SUCCESS, data = data,error= null)


class Loading<T>() : Resource<T>(status = Status.LOADING, data = null, error= null)


class DataError<T>(error: Error) : Resource<T>(status = Status.ERROR, data = null, error = error)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is DataError -> "Error[exception=${error?.code} ${error?.message}]"
            is Loading<T> -> "Loading"
        }
    }
}