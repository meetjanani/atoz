package com.growinginfotech.businesshub.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basicneedscorporation.basicneeds.rest.response.login.LoginResponse
import com.growinginfotech.businesshub.rest.ApiClient
import com.growinginfotech.businesshub.rest.ApiInterface

open class BaseViewModel  : ViewModel(){
     protected lateinit var apiService: ApiInterface
    val loginUserDetails = MutableLiveData<LoginResponse.UserDetails>()
    init {
        ApiClient().getClient()?.create(ApiInterface::class.java)?.let {
            apiService = it
        }
    }
}