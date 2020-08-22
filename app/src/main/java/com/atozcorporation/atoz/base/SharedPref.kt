package com.atozcorporation.atoz.base

import android.content.Context
import android.content.SharedPreferences
import com.atozcorporation.atoz.rest.response.login.LoginResponse
import com.google.gson.Gson

class SharedPref {
    private val ctx: Context? = null
    private val default_prefence: SharedPreferences? = null

    fun Save_Login_Data(
        ctx: Context, loginResponse: LoginResponse.UserDetails
    ) {
        val pref = ctx.getSharedPreferences("session", 0)
        val editor = pref.edit()
        editor.putBoolean("islogin", true) // Storing boolean - true/false
        editor.putString("loginResponse", Gson().toJson(loginResponse).toString())
        editor.commit()
    }

    fun Clear_Login_Data(
        ctx: Context, loginResponse: LoginResponse.UserDetails
    ) {
        val pref = ctx.getSharedPreferences("session", 0)
        val editor = pref.edit()
        editor.putBoolean("islogin", false) // Storing boolean - true/false
        editor.commit()
    }

    fun getIs_Login(ctx: Context): Boolean {
        val pref =
            ctx.getSharedPreferences("session", 0)
        return pref.getBoolean(
            "islogin",
            false
        ) // getting String
    }

    fun getLoginResponse(ctx: Context): LoginResponse.UserDetails {
        val pref =
            ctx.getSharedPreferences("session", 0)
        return Gson().fromJson(pref.getString("loginResponse", null), LoginResponse.UserDetails::class.java)
    }
}