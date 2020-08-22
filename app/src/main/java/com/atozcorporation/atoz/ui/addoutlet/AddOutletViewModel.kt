package com.atozcorporation.atoz.ui.addoutlet

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.spinnermaster.SpinnerMasterResponse
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
    val outletCategoryList = MutableLiveData<SpinnerMasterResponse>()
    val outletCityList = MutableLiveData<SpinnerMasterResponse>()
    val outletAreaList = MutableLiveData<SpinnerMasterResponse>()
    sealed class AddOutletAPIState {
        object Loading : AddOutletAPIState()
        data class SuccessOutletCategory(val data: SpinnerMasterResponse) : AddOutletAPIState()
        data class SuccessCity(val data: SpinnerMasterResponse) : AddOutletAPIState()
        data class SuccessArea(val data: SpinnerMasterResponse) : AddOutletAPIState()
        data class Success(val data: InsertResponse) : AddOutletAPIState()
        data class Failure(val throwable: Throwable) : AddOutletAPIState()
    }

    init {
        getOutletCategoryAPICall()
        getCityAPICall()
        getAreaAPICall()
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
        val call: Call<SpinnerMasterResponse> =
            apiService.Fill_SpinnerData("GetList", "outletCategory", "*", "isActive = 1")
        call.enqueue(object : Callback<SpinnerMasterResponse> {
            override fun onResponse(
                call: Call<SpinnerMasterResponse>,
                response: Response<SpinnerMasterResponse>

            ) {
                outletCategoryList.value = response.body()
            }

            override fun onFailure(
                call: Call<SpinnerMasterResponse>,
                t: Throwable
            ) {
                addOutletAPIState.postValue(AddOutletAPIState.Failure(t))
            }
        })
    }
    fun getCityAPICall() {
        addOutletAPIState.postValue(AddOutletAPIState.Loading)
        val call: Call<SpinnerMasterResponse> =
            apiService.Fill_SpinnerData("GetList", "city", "*", "isActive = 1")
        call.enqueue(object : Callback<SpinnerMasterResponse> {
            override fun onResponse(
                call: Call<SpinnerMasterResponse>,
                response: Response<SpinnerMasterResponse>

            ) {
                outletCityList.value = response.body()
            }

            override fun onFailure(
                call: Call<SpinnerMasterResponse>,
                t: Throwable
            ) {
                addOutletAPIState.postValue(AddOutletAPIState.Failure(t))
            }
        })
    }

    fun getAreaAPICall() {
        addOutletAPIState.postValue(AddOutletAPIState.Loading)
        val call: Call<SpinnerMasterResponse> =
            apiService.Fill_SpinnerData("GetList", "area", "*", "isActive = 1")
        call.enqueue(object : Callback<SpinnerMasterResponse> {
            override fun onResponse(
                call: Call<SpinnerMasterResponse>,
                response: Response<SpinnerMasterResponse>

            ) {
                outletAreaList.value = response.body()
            }

            override fun onFailure(
                call: Call<SpinnerMasterResponse>,
                t: Throwable
            ) {
                addOutletAPIState.postValue(AddOutletAPIState.Failure(t))
            }
        })
    }
}