package com.example.photoweatherapp.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photoweatherapp.model.SharedItem
import com.example.photoweatherapp.model.weatherHistory.WeatherHistoryItem
import com.example.photoweatherapp.shared.FACEBOOK_PACKAGE_NAME
import com.example.photoweatherapp.shared.TWITTER_PACKAGE_NAME
import timber.log.Timber

/**
 *Created by Yasser.Elnaggar on 4/10/2021
 */
class ShareViewModel(private val weatherItem: WeatherHistoryItem) : ViewModel() {

    private val _shareImage = MutableLiveData<SharedItem?>()
    val shareImage: LiveData<SharedItem?>
        get() = _shareImage


    init {
        Timber.i("${this.javaClass.name} created")
    }


    fun onFaceBookClick() {
        _shareImage.value = weatherItem.photoPath?.let { SharedItem(it, FACEBOOK_PACKAGE_NAME) }
        doneSharingImage()
    }

    fun onTwitterClick() {
        _shareImage.value = weatherItem.photoPath?.let { SharedItem(it, TWITTER_PACKAGE_NAME) }
        doneSharingImage()
    }

    fun onOtherClick() {
        _shareImage.value = weatherItem.photoPath?.let { SharedItem(it) }
        doneSharingImage()
    }

    private fun doneSharingImage() {
        _shareImage.value = null
    }

}