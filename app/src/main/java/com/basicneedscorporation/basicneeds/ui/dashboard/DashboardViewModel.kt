package com.basicneedscorporation.basicneeds.ui.dashboard

import androidx.lifecycle.MutableLiveData
import com.basicneedscorporation.basicneeds.rest.response.login.LoginResponse
import com.basicneedscorporation.basicneeds.ui.login.LoginViewModel
import com.growinginfotech.businesshub.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : BaseViewModel() {

    val loginAPIState = MutableLiveData<LoginAPIState>()
    /**
     * MainCategory API State
     */
    sealed class LoginAPIState {
        object Loading : LoginAPIState()
        data class SuccessLogin(val data: LoginResponse) : LoginAPIState()
        data class Failure(val throwable: Throwable) : LoginAPIState()
    }

    fun loginAPICall(batch : String, password : String) {
        val call: Call<LoginResponse> =
            apiService.login("GetByID", "outlet", "*", "(batchId = '${batch}' OR contactNumber = '${batch}') AND password = '${password}' AND isActive = 1")
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>

            ) {
                loginAPIState.postValue(LoginAPIState.SuccessLogin(response.body()))
            }

            override fun onFailure(
                call: Call<LoginResponse>,
                t: Throwable
            ) {
                loginAPIState.postValue(LoginAPIState.Failure(t))
            }
        })
    }
}