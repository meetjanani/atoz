package com.growinginfotech.businesshub.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.Toast
import com.atozcorporation.atoz.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

inline fun <reified T : Activity> Activity.navigateTo(func: Intent.() -> Unit = {}) =
    this.startActivity(Intent(this, T::class.java).apply(func))

fun String.defaultToast(context : Context){
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
        .placeholder(R.drawable.ic_notifications_black_24dp)
        .error(R.drawable.ic_notifications_black_24dp)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .skipMemoryCache(true)
        .into(imageView)
}