package com.atozcorporation.atoz.ui.manageorder.cart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.base.offlinedb.Order_Summery_Ofline_Bean
import com.atozcorporation.atoz.rest.response.login.LoginResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.base.OrderId
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel : BaseViewModel() {

    val productsListForPlaceOrder = MutableLiveData<List<Order_Summery_Ofline_Bean>>()
    val userDetailsOfOrderBy = MutableLiveData<LoginResponse.UserDetails>()
    val userDetailsOfOrderFor = MutableLiveData<LoginResponse.UserDetails>()
    val placeOrderAPIState = MutableLiveData<PlaceOrderAPIState>()

    sealed class PlaceOrderAPIState {
        object Loading : PlaceOrderAPIState()
        data class Success(val data: InsertResponse) : PlaceOrderAPIState()
        data class SuccessProductSubmited(val productId: Int) : PlaceOrderAPIState()
        data class Failure(val throwable: Throwable) : PlaceOrderAPIState()
    }

    fun generateNewOrderID() {
        placeOrderAPIState.postValue(PlaceOrderAPIState.Loading)
        val call: Call<InsertResponse> =
            apiService.Fetch_New_Order_ID("Get_New_Order_ID", "1")
        call.enqueue(object : Callback<InsertResponse> {
            override fun onResponse(
                call: Call<InsertResponse>,
                response: Response<InsertResponse>

            ) {
                OrderId = response.body().message
                New_Order_Submit_Api_Call(OrderId)
                placeOrderAPIState.postValue(PlaceOrderAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<InsertResponse>,
                t: Throwable
            ) {
                placeOrderAPIState.postValue(PlaceOrderAPIState.Failure(t))
            }
        })
    }

    fun New_Order_Submit_Api_Call(OrderID: String?) {
        placeOrderAPIState.postValue(PlaceOrderAPIState.Loading)
        productsListForPlaceOrder.value?.let { productList ->
            productList.map {productDetails ->
                val call: Call<InsertResponse> = apiService.Add_New_Order(
                    "PlaceOrder",
                    OrderID,
                    "Verifying",
                    "1",
                    productDetails.productId,
                    productDetails.productName,
                    productDetails.productCategoryId,
                    productDetails.productCategoryName,
                    productDetails.productBrandId,
                    productDetails.productBrandName,
                    productDetails.productQty,
                    productDetails.productPrice,
                    productDetails.productTotal,
                    productDetails.orderTotal, // orderTotal
                    userDetailsOfOrderBy.value?.contactNumber,
                    userDetailsOfOrderBy.value?.address1 + " \n" + userDetailsOfOrderBy.value?.address2,
                    "${userDetailsOfOrderBy.value?.name},${userDetailsOfOrderBy.value?.personName}", // order By
                    userDetailsOfOrderBy.value?.id.toString(),
                    userDetailsOfOrderBy.value?.batchId,
                    "${userDetailsOfOrderFor.value?.name},${userDetailsOfOrderFor.value?.personName}", // order For
                    userDetailsOfOrderFor.value?.id.toString(),
                    userDetailsOfOrderFor.value?.batchId,
                    productDetails.productUrl1,
                    "",
                    userDetailsOfOrderBy.value?.pinCode + ""
                )
                call.enqueue(object : Callback<InsertResponse> {
                    override fun onResponse(
                        call: Call<InsertResponse>,
                        response: Response<InsertResponse>
                    ) {
                        placeOrderAPIState.postValue(
                            PlaceOrderAPIState.SuccessProductSubmited(
                                productDetails.productId.toInt()
                            )
                        )
                    }

                    override fun onFailure(call: Call<InsertResponse>, t: Throwable) {
                        placeOrderAPIState.postValue(PlaceOrderAPIState.Failure(t))
                    }
                })
            }
        }
        // razorPayGenerateOrder()
    }

    fun getUserDetailsAPICall(batch : String) {
        val call: Call<LoginResponse> =
            apiService.login("GetByID", "outlet", "*", "batchId = '${batch}' OR contactNumber = '${batch}'")
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>

            ) {
                userDetailsOfOrderBy.value = response.body().data
                Log.v("userDetails", response.body().data.batchId.toString())
            }

            override fun onFailure(
                call: Call<LoginResponse>,
                t: Throwable
            ) {
                //
            }
        })
    }
}