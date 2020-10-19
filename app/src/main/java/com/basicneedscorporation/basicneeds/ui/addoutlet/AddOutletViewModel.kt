package com.basicneedscorporation.basicneeds.ui.addoutlet

import androidx.lifecycle.MutableLiveData
import com.basicneedscorporation.basicneeds.rest.response.outlet.OutletDetailsResponse
import com.basicneedscorporation.basicneeds.rest.response.outlet.OutletListResponse
import com.basicneedscorporation.basicneeds.rest.response.spinnermaster.SpinnerMasterResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.base.initWith
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOutletViewModel : BaseViewModel() {
    val outletUserRoll = MutableLiveData<String>().initWith("3")
    val loginUserRoll = MutableLiveData<String>().initWith("3")
    val addOutletAPIState = MutableLiveData<AddOutletAPIState>()
    val isEditOutlet = MutableLiveData<Boolean>()
    val outletDetails = MutableLiveData<OutletListResponse.Outlet>()

    /**
     * MainCategory API State
     */
    val outletCategoryList = MutableLiveData<SpinnerMasterResponse>()
    val outletCityList = MutableLiveData<SpinnerMasterResponse>()
    val outletAreaList = MutableLiveData<SpinnerMasterResponse>()
    val outletOnList = MutableLiveData<SpinnerMasterResponse>()
    val brandList = MutableLiveData<SpinnerMasterResponse>()

    sealed class AddOutletAPIState {
        object Loading : AddOutletAPIState()
        data class Success(val data: InsertResponse) : AddOutletAPIState()
        data class SuccessVerifyOutlet(val data: Boolean) : AddOutletAPIState()
        data class Failure(val throwable: Throwable) : AddOutletAPIState()
    }

    init {
        isEditOutlet.value = false
        getCityAPICall()
        getAreaAPICall()
        getOutletOnAPICall()
    }

    fun addOutletAPICall(col: String, colData: String) {
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

    fun verifyOutletAPICall(batchId: String, contactNumber: String) {
        addOutletAPIState.postValue(AddOutletAPIState.Loading)
        val call: Call<OutletDetailsResponse> =
            apiService.verifyOutletDetails(
                "GetByID",
                "outlet",
                "*",
                "batchId = '${batchId}' or contactNumber = '${contactNumber}' "
            )
        call.enqueue(object : Callback<OutletDetailsResponse> {
            override fun onResponse(
                call: Call<OutletDetailsResponse>,
                response: Response<OutletDetailsResponse>

            ) {
                addOutletAPIState.postValue(AddOutletAPIState.SuccessVerifyOutlet(response.body().status == 1))
            }

            override fun onFailure(
                call: Call<OutletDetailsResponse>,
                t: Throwable
            ) {
                addOutletAPIState.postValue(AddOutletAPIState.Failure(t))
            }
        })
    }

    fun updateOutletAPICall(colAndData: String, whereClouse: String) {
        addOutletAPIState.postValue(AddOutletAPIState.Loading)
        val call: Call<InsertResponse> =
            apiService.Common_Master_Update("Update", "outlet", colAndData, whereClouse)
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
        var whereCondition = "isActive = 1"
        if(loginUserRoll.value == "1"){
            whereCondition = "true"
        }
        addOutletAPIState.postValue(AddOutletAPIState.Loading)
        val call: Call<SpinnerMasterResponse> =
            apiService.Fill_SpinnerData("GetList", "outletCategory", "*", whereCondition)
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

    private fun getOutletOnAPICall() {
        addOutletAPIState.postValue(AddOutletAPIState.Loading)
        val call: Call<SpinnerMasterResponse> =
            apiService.Fill_SpinnerData("GetList", "outletOn", "*", "isActive = 1")
        call.enqueue(object : Callback<SpinnerMasterResponse> {
            override fun onResponse(
                call: Call<SpinnerMasterResponse>,
                response: Response<SpinnerMasterResponse>

            ) {
                outletOnList.value = response.body()
            }

            override fun onFailure(
                call: Call<SpinnerMasterResponse>,
                t: Throwable
            ) {
                addOutletAPIState.postValue(AddOutletAPIState.Failure(t))
            }
        })
    }

    fun getBrandAPICall() {
        addOutletAPIState.postValue(AddOutletAPIState.Loading)
        val call: Call<SpinnerMasterResponse> =
            apiService.Fill_SpinnerData("GetList", "productBrand", "*", "isActive = 1")
        call.enqueue(object : Callback<SpinnerMasterResponse> {
            override fun onResponse(
                call: Call<SpinnerMasterResponse>,
                response: Response<SpinnerMasterResponse>

            ) {
                brandList.value = response.body()
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