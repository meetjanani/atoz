package com.basicneedscorporation.basicneeds.ui.changepassword

import androidx.lifecycle.MutableLiveData
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordViewModel : BaseViewModel() {
    val changePasswordAPIState = MutableLiveData<ChangePasswordAPIState>()
    sealed class ChangePasswordAPIState {
        object Loading : ChangePasswordAPIState()
        data class Success(val data: InsertResponse) : ChangePasswordAPIState()
        data class Failure(val throwable: Throwable) : ChangePasswordAPIState()
    }


    fun changePasswordAPICall(colAndData: String, whereClouse: String) {
        changePasswordAPIState.postValue(ChangePasswordAPIState.Loading)
        val call: Call<InsertResponse> =
            apiService.Common_Master_Update("Update", "outlet", colAndData, whereClouse)
        call.enqueue(object : Callback<InsertResponse> {
            override fun onResponse(
                call: Call<InsertResponse>,
                response: Response<InsertResponse>

            ) {
                changePasswordAPIState.postValue(ChangePasswordAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<InsertResponse>,
                t: Throwable
            ) {
                changePasswordAPIState.postValue(ChangePasswordAPIState.Failure(t))
            }
        })
    }
}