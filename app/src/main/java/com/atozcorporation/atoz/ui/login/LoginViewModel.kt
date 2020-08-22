package com.atozcorporation.atoz.ui.login

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.login.LoginResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : BaseViewModel() {
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
        loginAPIState.postValue(LoginAPIState.Loading)
        val call: Call<LoginResponse> =
            apiService.login("GetByID", "outlet", "*", "(batchId = '${batch}' OR contactNumber = '${batch}') AND password = '${password}'")
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