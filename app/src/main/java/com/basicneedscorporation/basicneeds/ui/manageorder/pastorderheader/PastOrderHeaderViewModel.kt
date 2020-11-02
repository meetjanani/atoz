package com.basicneedscorporation.basicneeds.ui.manageorder.pastorderheader

import androidx.lifecycle.MutableLiveData
import com.basicneedscorporation.basicneeds.rest.response.pastorder.PastOrderHeaderResponse
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

    fun getPastOrderAPICall(loginUserBatchId: String, rollId : Int = 0, fromDate : String, toDate : String) {
        pastOrderAPIState.postValue(PastOrderAPIState.Loading)
        val call: Call<PastOrderHeaderResponse>
        if(rollId == 1){
            call = apiService.getPastOrdersForAdmin("Display_past_Orders_List_For_Admin", fromDate, toDate)
        } else {
            call = apiService.getPastOrdersByLoginUser("Display_past_Orders_List", loginUserBatchId, fromDate, toDate)
        }
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