package com.basicneedscorporation.basicneeds.ui.manageorder.pastorderdproductdetails

import androidx.lifecycle.MutableLiveData
import com.basicneedscorporation.basicneeds.rest.response.pastorder.OrderDetailsResponse
import com.basicneedscorporation.basicneeds.rest.response.spinnermaster.SpinnerMasterResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PastOrderedProductDetailsViewModel : BaseViewModel() {
    val orderWiseProductListAPIState = MutableLiveData<OrderWiseProductListAPIState>()
    val orderId = MutableLiveData<String>()
    val orderStatus = MutableLiveData<SpinnerMasterResponse>()
    sealed class OrderWiseProductListAPIState {
        object Loading : OrderWiseProductListAPIState()
        data class Success(val data: OrderDetailsResponse) : OrderWiseProductListAPIState()
        data class Failure(val throwable: Throwable) : OrderWiseProductListAPIState()
    }

    init {
        getOrderStatusAPICall()
    }
    fun getOrderStatusAPICall() {
        val call: Call<SpinnerMasterResponse> =
            apiService.Fill_SpinnerData("GetList", "Order_Status", "ID as id, Status as name", "Is_Active = 1")
        call.enqueue(object : Callback<SpinnerMasterResponse> {
            override fun onResponse(
                call: Call<SpinnerMasterResponse>,
                response: Response<SpinnerMasterResponse>

            ) {
                orderStatus.value = response.body()
                getOrderWiseProductListAPICall()
            }

            override fun onFailure(
                call: Call<SpinnerMasterResponse>,
                t: Throwable
            ) {
            }
        })
    }

    fun getOrderWiseProductListAPICall() {
        orderWiseProductListAPIState.postValue(OrderWiseProductListAPIState.Loading)
        val call: Call<OrderDetailsResponse> =
            apiService.getOrderWiseProducs("Display_past_Order_By_OrderID_Old", orderId.value)
        call.enqueue(object : Callback<OrderDetailsResponse> {
            override fun onResponse(
                call: Call<OrderDetailsResponse>,
                response: Response<OrderDetailsResponse>

            ) {
                orderWiseProductListAPIState.postValue(OrderWiseProductListAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<OrderDetailsResponse>,
                t: Throwable
            ) {
                orderWiseProductListAPIState.postValue(OrderWiseProductListAPIState.Failure(t))
            }
        })
    }

    fun updateOrderStatusAPICall(statusId : Int, statusName : String) {
        val call: Call<InsertResponse> =
            apiService.Common_Master_Update("Update", "Orders", "`Order_Status` = '${statusName}', `Order_Type` = '${statusId}'", "Order_ID = '${orderId.value}'")
        call.enqueue(object : Callback<InsertResponse> {
            override fun onResponse(
                call: Call<InsertResponse>,
                response: Response<InsertResponse>

            ) {
                getOrderWiseProductListAPICall()
            }

            override fun onFailure(
                call: Call<InsertResponse>,
                t: Throwable
            ) {
                //
            }
        })
    }
}