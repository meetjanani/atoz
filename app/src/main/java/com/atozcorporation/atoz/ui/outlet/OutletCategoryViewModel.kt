package com.atozcorporation.atoz.ui.outlet

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.outlet.OutletCategoryResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutletCategoryViewModel : BaseViewModel() {
    val outletAPIState = MutableLiveData<OutletAPIState>()
    /**
     * MainCategory API State
     */
    sealed class OutletAPIState {
        object Loading : OutletAPIState()
        data class Success(val data: OutletCategoryResponse) : OutletAPIState()
        data class Failure(val throwable: Throwable) : OutletAPIState()
    }
    init {
        getOutletCategoryAPICall()
    }

    fun getOutletCategoryAPICall() {
        outletAPIState.postValue(OutletAPIState.Loading)
        val call: Call<OutletCategoryResponse> =
            apiService.getOutletCategory("GetList", "outletCategory", "*", "isActive = 1")
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