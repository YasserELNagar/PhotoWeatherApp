package com.example.photoweatherapp.shared.helper

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.photoweatherapp.R
import com.example.photoweatherapp.model.weatherHistory.WeatherHistoryItem
import com.example.photoweatherapp.ui.weatherHistory.adapter.WeatherHistoryItemsAdapter
import java.io.File

/**
 *Created by Yasser.Elnaggar on 4/9/2021
 */


@BindingAdapter("historyListItems")
fun RecyclerView.historyListItems(items: List<WeatherHistoryItem>?) {
    val adapter = this.adapter as WeatherHistoryItemsAdapter
    adapter.submitList(items)
}


@BindingAdapter(value = ["imagePath", "isThumbnail"], requireAll = true)
fun ImageView.loadImageFromPath(path: String?, isThumbnail: Boolean) {
    path?.let {
        this.loadImageFromPathByGlide(it, isThumbnail, width, height)
    }
}


fun ImageView.loadImageFromPathByGlide(
    path: String,
    isThumbnail: Boolean,
    width: Int = 0,
    height: Int = 0
) {

    val imageUri = Uri.fromFile(File(path))

    if (!isThumbnail) {
        Glide
            .with(context)
            .load(imageUri)
            .error(R.drawable.img_no_image)
            .centerCrop()
            .into(this)

    } else {
        Glide
            .with(context)
            .load(imageUri)
            .override(width.pxFromDp(context), height.pxFromDp(context))
            .error(R.drawable.img_no_image)
            .centerCrop()
            .into(this)
    }


}


fun getDrawableProgress(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context)?.apply {
        backgroundColor = ContextCompat.getColor(context, android.R.color.transparent)
        strokeWidth = 10.dpFromPx(context).toFloat()
        centerRadius = 50.dpFromPx(context).toFloat()
        colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                R.color.black,
                BlendModeCompat.SRC_ATOP
            )

        start()
    }
}