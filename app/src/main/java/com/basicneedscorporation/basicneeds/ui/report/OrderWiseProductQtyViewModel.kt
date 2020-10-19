package com.basicneedscorporation.basicneeds.ui.report

import androidx.lifecycle.MutableLiveData
import com.basicneedscorporation.basicneeds.rest.response.report.OrderWiseProductQtyResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderWiseProductQtyViewModel : BaseViewModel() {
    val orderWiseProductQtyAPIState = MutableLiveData<OrderWiseProductQtyAPIState>()

    sealed class OrderWiseProductQtyAPIState {
        object Loading : OrderWiseProductQtyAPIState()
        data class Success(val data: OrderWiseProductQtyResponse) : OrderWiseProductQtyAPIState()
        data class Failure(val throwable: Throwable) : OrderWiseProductQtyAPIState()
    }

    fun getOrderWiseProductQtyAPICall(brandId: String, isAllProductList : Boolean = false) {
        var brandIdWhereCondition : String
        if(isAllProductList){
            brandIdWhereCondition = ""
        }
        else {
            brandIdWhereCondition = "AND `ProductBrandId` = ${brandId}"
        }
        var query  = "SELECT *, sum(Qty) as ProductWiseOrderQty  FROM `Orders` \n" +
                "    WHERE \n" +
                "        `Order_Type` = 1  ${brandIdWhereCondition}\n" +
                "    group BY Product_ID"
        orderWiseProductQtyAPIState.postValue(OrderWiseProductQtyAPIState.Loading)
        val call: Call<OrderWiseProductQtyResponse> =
            apiService.OrderWiseProductQty("Dynamic", query)
        call.enqueue(object : Callback<OrderWiseProductQtyResponse> {
            override fun onResponse(
                call: Call<OrderWiseProductQtyResponse>,
                response: Response<OrderWiseProductQtyResponse>

            ) {
                orderWiseProductQtyAPIState.postValue(OrderWiseProductQtyAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<OrderWiseProductQtyResponse>,
                t: Throwable
            ) {
                orderWiseProductQtyAPIState.postValue(OrderWiseProductQtyAPIState.Failure(t))
            }
        })
    }
}