package com.atozcorporation.atoz.ui.outlet

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.outlet.OutletListResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.base.CurrentSelectedOutletCategoryId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutletViewModel : BaseViewModel() {
    val outletAPIState = MutableLiveData<OutletAPIState>()
    /**
     * MainCategory API State
     */
    sealed class OutletAPIState {
        object Loading : OutletAPIState()
        data class Success(val data: OutletListResponse) : OutletAPIState()
        data class Failure(val throwable: Throwable) : OutletAPIState()
    }
    init {
        getSubCategoryAPICall()
    }

    fun getSubCategoryAPICall() {
        outletAPIState.postValue(OutletAPIState.Loading)
        val call: Call<OutletListResponse> =
            apiService.getOutletList("GetList", "outlet", "*", "categoryId = $CurrentSelectedOutletCategoryId")
        call.enqueue(object : Callback<OutletListResponse> {
            override fun onResponse(
                call: Call<OutletListResponse>,
                response: Response<OutletListResponse>

            ) {
                outletAPIState.postValue(OutletAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<OutletListResponse>,
                t: Throwable
            ) {
                outletAPIState.postValue(OutletAPIState.Failure(t))
            }
        })
    }
}