package com.example.photoweatherapp.shared.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photoweatherapp.R
import com.google.android.gms.location.*
import timber.log.Timber

const val GPS_REQUEST_CODE = 4043


class Location {

    companion object{
        //get the current location from using Google FusedLocationProvider

        private lateinit var  mFusedLocationProviderClient :FusedLocationProviderClient

        operator fun invoke(context:Context){
             mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        }

        @SuppressLint("MissingPermission")
        fun getMyLocation(): LiveData<Location?> {

            Timber.i( "getting My Device Location")

            val mutableLocation = MutableLiveData<Location?>()

            val locationCallback: LocationCallback by lazy {
                object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        result?.let {
                            for (location in result.locations) {
                                mutableLocation.postValue(location)

                                //cancel location updates after getting location
                                mFusedLocationProviderClient.removeLocationUpdates(this)
                                break
                            }
                        }
                    }
                }
            }
            val locationUpdateRequest: LocationRequest by lazy {
                LocationRequest.create().apply {
                    this.interval = 1000
                    this.fastestInterval = 1000
                    this.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                }
            }

            mFusedLocationProviderClient.requestLocationUpdates(locationUpdateRequest,locationCallback,null)

            return mutableLocation
        }

    }

}

fun Activity.checkGps(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        true
    } else {
        showGPSDisabledAlertToUser()
        false
    }
}

private fun Activity.showGPSDisabledAlertToUser() {
    val alertDialogBuilder = AlertDialog.Builder(this)

    alertDialogBuilder.setMessage(getString(R.string.please_open_gps))
        .setCancelable(false)
        .setPositiveButton(getString(R.string.gps_request)) { dialog, id ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(
                intent,
                GPS_REQUEST_CODE
            )
        }
    alertDialogBuilder.setNegativeButton(
        getString(R.string.cancel)
    ) { dialog, id -> }
    val alert = alertDialogBuilder.create()

    alert.show()
}

