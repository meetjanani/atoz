package com.basicneedscorporation.basicneeds.ui.outlet

import androidx.lifecycle.MutableLiveData
import com.basicneedscorporation.basicneeds.rest.response.outlet.OutletListResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.base.CurrentSelectedOutletCategoryId
import com.growinginfotech.businesshub.base.initWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutletViewModel : BaseViewModel() {
    val outletUserRoll = MutableLiveData<String>().initWith("3")
    val outletAPIState = MutableLiveData<OutletAPIState>()
    val outletListRecords = MutableLiveData<MutableList<OutletListResponse.Outlet>>().initWith(
        mutableListOf())
    sealed class OutletAPIState {
        object Loading : OutletAPIState()
        data class Success(val data: OutletListResponse) : OutletAPIState()
        data class Failure(val throwable: Throwable) : OutletAPIState()
    }

    fun getCategoryWiseOutletList() {
        var whereCondition = "AND `userId` = ${loginUserDetails.value?.id} "
        if(outletUserRoll.value == "1"){
            whereCondition = " "
        }
        outletAPIState.postValue(OutletAPIState.Loading)
        val call: Call<OutletListResponse> =
            apiService.getOutletList("GetList", "outlet", "*", " `categoryId` = $CurrentSelectedOutletCategoryId $whereCondition ")
        call.enqueue(object : Callback<OutletListResponse> {
            override fun onResponse(
                call: Call<OutletListResponse>,
                response: Response<OutletListResponse>

            ) {
                outletListRecords.value?.addAll(response.body().data)
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