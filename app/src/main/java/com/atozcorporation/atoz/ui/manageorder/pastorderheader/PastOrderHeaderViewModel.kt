package com.atozcorporation.atoz.ui.manageorder.pastorderheader

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.pastorder.PastOrderHeaderResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PastOrderHeaderViewModel : BaseViewModel() {
    val pastOrderAPIState = MutableLiveData<PastOrderAPIState>()

    sealed class PastOrderAPIState {
        object Loading : PastOrderAPIState()
        data class Success(val data: PastOrderHeaderResponse) : PastOrderAPIState()
        data class Failure(val throwable: Throwable) : PastOrderAPIState()
    }

    fun getPastOrderAPICall(loginUserBatchId: String) {
        pastOrderAPIState.postValue(PastOrderAPIState.Loading)
        val call: Call<PastOrderHeaderResponse> =
            apiService.getPastOrdersByLoginUser("Display_past_Orders_List", loginUserBatchId)
        call.enqueue(object : Callback<PastOrderHeaderResponse> {
            override fun onResponse(
                call: Call<PastOrderHeaderResponse>,
                response: Response<PastOrderHeaderResponse>

            ) {
                pastOrderAPIState.postValue(PastOrderAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<PastOrderHeaderResponse>,
                t: Throwable
            ) {
                pastOrderAPIState.postValue(PastOrderAPIState.Failure(t))
            }
        })
    }
}