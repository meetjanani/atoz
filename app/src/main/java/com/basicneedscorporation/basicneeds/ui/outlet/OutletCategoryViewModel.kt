package com.basicneedscorporation.basicneeds.ui.outlet

import androidx.lifecycle.MutableLiveData
import com.basicneedscorporation.basicneeds.rest.response.outlet.OutletCategoryResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.base.initWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutletCategoryViewModel : BaseViewModel() {
    val outletUserRoll = MutableLiveData<String>().initWith("3")
    val outletAPIState = MutableLiveData<OutletAPIState>()
    /**
     * MainCategory API State
     */
    sealed class OutletAPIState {
        object Loading : OutletAPIState()
        data class Success(val data: OutletCategoryResponse) : OutletAPIState()
        data class Failure(val throwable: Throwable) : OutletAPIState()
    }

    fun getOutletCategoryAPICall() {
        var whereCondition = "isActive = 1"
        if(outletUserRoll.value == "1"){
            whereCondition = "true"
        }
        outletAPIState.postValue(OutletAPIState.Loading)
        val call: Call<OutletCategoryResponse> =
            apiService.getOutletCategory("GetList", "outletCategory", "*", whereCondition)
        call.enqueue(object : Callback<OutletCategoryResponse> {
            override fun onResponse(
                call: Call<OutletCategoryResponse>,
                response: Response<OutletCategoryResponse>

            ) {
                outletAPIState.postValue(OutletAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<OutletCategoryResponse>,
                t: Throwable
            ) {
                outletAPIState.postValue(OutletAPIState.Failure(t))
            }
        })
    }
}