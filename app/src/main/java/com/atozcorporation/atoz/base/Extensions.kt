package com.growinginfotech.businesshub.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast

inline fun <reified T : Activity> Activity.navigateTo(func: Intent.() -> Unit = {}) =
    this.startActivity(Intent(this, T::class.java).apply(func))

fun String.defaultToast(context : Context){
    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}