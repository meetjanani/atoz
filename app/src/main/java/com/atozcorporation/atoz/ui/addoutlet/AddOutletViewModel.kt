package com.atozcorporation.atoz.ui.addoutlet

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.outlet.OutletCategoryResponse
import com.atozcorporation.atoz.rest.response.outlet.OutletListResponse
import com.atozcorporation.atoz.ui.outlet.OutletCategoryViewModel
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOutletViewModel : BaseViewModel() {
    val addOutletAPIState = MutableLiveData<AddOutletAPIState>()
    /**
     * MainCategory API State
     */
    val outletCategoryList = MutableLiveData<OutletCategoryResponse>()
    sealed class AddOutletAPIState {
        object Loading : AddOutletAPIState()
        data class SuccessOutletCategory(val data: OutletCategoryResponse) : AddOutletAPIState()
        data class Success(val data: InsertResponse) : AddOutletAPIState()
        data class Failure(val throwable: Throwable) : AddOutletAPIState()
    }

    init {
        getOutletCategoryAPICall()
    }
    fun addOutletAPICall(col : String, colData : String) {
        addOutletAPIState.postValue(AddOutletAPIState.Loading)
        val call: Call<InsertResponse> =
            apiService.Common_Master_Insert("Insert", "outlet", col, colData)
        call.enqueue(object : Callback<InsertResponse> {
            override fun onResponse(
                call: Call<InsertResponse>,
                response: Response<InsertResponse>

            ) {
                addOutletAPIState.postValue(AddOutletAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<InsertResponse>,
                t: Throwable
            ) {
                addOutletAPIState.postValue(AddOutletAPIState.Failure(t))
            }
        })
    }

    fun getOutletCategoryAPICall() {
        addOutletAPIState.postValue(AddOutletAPIState.Loading)
        val call: Call<OutletCategoryResponse> =
            apiService.getOutletCategory("GetList", "outletCategory", "*", "isActive = 1")
        call.enqueue(object : Callback<OutletCategoryResponse> {
            override fun onResponse(
                call: Call<OutletCategoryResponse>,
                response: Response<OutletCategoryResponse>

            ) {
                outletCategoryList.value = response.body()
                addOutletAPIState.postValue(AddOutletAPIState.SuccessOutletCategory(response.body()))
            }

            override fun onFailure(
                call: Call<OutletCategoryResponse>,
                t: Throwable
            ) {
                addOutletAPIState.postValue(AddOutletAPIState.Failure(t))
            }
        })
    }
}