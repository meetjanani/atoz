package com.atozcorporation.atoz.base

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.provider.SyncStateContract
import androidx.appcompat.app.AppCompatActivity
import com.atozcorporation.atoz.rest.response.login.LoginResponse
import com.google.gson.Gson

abstract class BaseActivity : AppCompatActivity() {
    var activity: Activity? = null
    var preferences: SharedPreferences? = null
    var loginUser: LoginResponse.UserDetails? = null

    open fun setActivityContext(activity: Activity?) {
        this.activity = activity
        preferences = activity!!.getSharedPreferences("shared_preference", 0)
        getSharedPreferenceloginUser()
    }
   //  protected val loginUser = SharedPref().getLoginResponse(this)

    open fun setSharedPreferenceloginUser(value: LoginResponse.UserDetails?, boolean: Boolean) {
        val gson = Gson()
        val tokenSchemaJson = gson.toJson(value)
        val editor: SharedPreferences.Editor = preferences?.edit()!!
        editor.putString("loginUser", tokenSchemaJson)
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

    fun getIs_Login(): Boolean {
        return preferences?.getBoolean(
            "islogin",
            false
        ) ?: false
    }

}