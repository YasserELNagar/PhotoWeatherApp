package com.example.photoweatherapp.shared.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.photoweatherapp.R
import com.stfalcon.frescoimageviewer.ImageViewer
import java.io.File

/**
 *Created by Yasser.Elnaggar on 4/9/2021
 */


fun Int.dpFromPx(context: Context): Int {
    return (this / context.resources.displayMetrics.density).toInt()
}

fun Int.pxFromDp(context: Context): Int {
    return this * context.resources.displayMetrics.density.toInt()
}

fun Activity.shareImage(imagePath: String, packageName: String? = null) {

    val imageFile=File(imagePath)

    val imageUri = FileProvider.getUriForFile(
        this,
        "com.example.photoweatherapp.fileprovider",
        imageFile)

    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, imageUri)
        packageName?.let {
            setPackage(it)
        }
        type = "image/jpeg"
    }
    startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.sahre_to)))
}

fun Activity.zoom(position: Int, imageUrls: List<String>) {

    val imageUris = imageUrls.map {
        try {
            return@map File(it).toUri()
        } catch (e: Exception) {
            return@map null
        }
    }

    ImageViewer.Builder<Any?>(this, imageUris)
        .setStartPosition(position)
        .hideStatusBar(false)
        .allowZooming(true)
        .allowSwipeToDismiss(true)
        .setBackgroundColorRes(android.R.color.black)
        .show()

}


