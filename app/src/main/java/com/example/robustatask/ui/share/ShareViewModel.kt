package com.example.robustatask.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.robustatask.model.SharedItem
import com.example.robustatask.model.weatherHistory.WeatherHistoryItem
import com.example.robustatask.shared.FACEBOOK_PACKAGE_NAME
import com.example.robustatask.shared.TWITTER_PACKAGE_NAME
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