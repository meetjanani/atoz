package com.atozcorporation.atoz.ui.manageorder.pastorderdproductdetails

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.pastorder.OrderDetailsResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PastOrderedProductDetailsViewModel : BaseViewModel() {
    val orderWiseProductListAPIState = MutableLiveData<OrderWiseProductListAPIState>()

    sealed class OrderWiseProductListAPIState {
        object Loading : OrderWiseProductListAPIState()
        data class Success(val data: OrderDetailsResponse) : OrderWiseProductListAPIState()
        data class Failure(val throwable: Throwable) : OrderWiseProductListAPIState()
    }

    fun getOrderWiseProductListAPICall(orderId: String) {
        orderWiseProductListAPIState.postValue(OrderWiseProductListAPIState.Loading)
        val call: Call<OrderDetailsResponse> =
            apiService.getOrderWiseProducs("Display_past_Order_By_OrderID_Old", orderId)
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
}