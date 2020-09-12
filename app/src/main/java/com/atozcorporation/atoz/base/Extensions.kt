package com.growinginfotech.businesshub.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

inline fun <reified T : Activity> Activity.navigateTo(func: Intent.() -> Unit = {}) =
    this.startActivity(Intent(this, T::class.java).apply(func))

fun String.defaultToast(context: Context){
    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}

inline fun <reified T : Activity> Activity.navigateTo() =
    this.startActivity(Intent(this, T::class.java))

inline fun <reified T : Activity> Activity.navigateToAndFinish() {
    this.apply {
        startActivity(Intent(this, T::class.java))
        finish()
    }
}

fun loadImage(
    url: String?,
    imageView: ImageView,
    context: Context
) {
    Glide.with(context)
        .asBitmap()
        .optionalCenterCrop()
        .placeholder(R.drawable.ic_add_image)
        .error(R.drawable.ic_add_image)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .skipMemoryCache(true)
        .into(imageView)
}

fun <T> MutableLiveData<T>.initWith(data: T): MutableLiveData<T> = this.apply {
    value = data
}

@SuppressLint("NewApi")
fun getDateTime(s: String): String? {
    val localDateTime: LocalDateTime = LocalDateTime.parse(s)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    val output: String = formatter.format(localDateTime)
    return output
}

fun String.getFormatedDateTime(inputDateFormat: String, outputDateFormat: String): String {
    val date = SimpleDateFormat(inputDateFormat, Locale.getDefault()).parse(this)
    date?.let {
        return SimpleDateFormat(outputDateFormat, Locale.getDefault()).format(it)
    } ?: return ""
}