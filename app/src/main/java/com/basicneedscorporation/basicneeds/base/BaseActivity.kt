package com.basicneedscorporation.basicneeds.base

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.basicneedscorporation.basicneeds.rest.response.login.LoginResponse
import com.google.gson.Gson

abstract class BaseActivity : AppCompatActivity() {
    var activity: Activity? = null
    var preferences: SharedPreferences? = null
    var loginUser: LoginResponse.UserDetails? = null
    var orderFor: LoginResponse.UserDetails? = null

    open fun setActivityContext(activity: Activity?) {
        this.activity = activity
        preferences = activity!!.getSharedPreferences("shared_preference", 0)
        getSharedPreferenceloginUser()
        getOrderForUser()
    }
   //  protected val loginUser = SharedPref().getLoginResponse(this)

    open fun setSharedPreferenceloginUser(value: LoginResponse.UserDetails?, boolean: Boolean) {
        val gson = Gson()
        val loginUserDetails = gson.toJson(value)
        val editor: SharedPreferences.Editor = preferences?.edit()!!
        editor.putString("loginUser", loginUserDetails)
        editor.putBoolean("islogin", boolean) // Storing boolean - true/false
        editor.commit()
        getSharedPreferenceloginUser()
    }

    open fun getSharedPreferenceloginUser() {
        this.loginUser =
            Gson().fromJson(
                preferences?.getString("loginUser", ""),
                LoginResponse.UserDetails::class.java
            )
    }

    open fun setOrderForUser(value: LoginResponse.UserDetails?) {
        val gson = Gson()
        val loginUserDetails = gson.toJson(value)
        val editor: SharedPreferences.Editor = preferences?.edit()!!
        editor.putString("orderForUser", loginUserDetails)
        editor.commit()
        getOrderForUser()
    }

    open fun getOrderForUser() {
        this.orderFor =
            Gson().fromJson(
                preferences?.getString("orderForUser", ""),
                LoginResponse.UserDetails::class.java
            )
    }

    fun getIs_Login(): Boolean {
        return preferences?.getBoolean(
            "islogin",
            false
        ) ?: false
    }

}