package com.growinginfotech.businesshub.base

import androidx.lifecycle.ViewModel
import com.growinginfotech.businesshub.rest.ApiClient
import com.growinginfotech.businesshub.rest.ApiInterface

open class BaseViewModel  : ViewModel(){
     protected lateinit var apiService: ApiInterface
    init {
        ApiClient().getClient()?.create(ApiInterface::class.java)?.let {
            apiService = it
        }
    }
}