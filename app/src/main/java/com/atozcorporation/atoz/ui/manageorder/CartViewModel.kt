package com.atozcorporation.atoz.ui.manageorder

import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.base.offlinedb.Order_Summery_Ofline_Bean
import com.atozcorporation.atoz.rest.response.login.LoginResponse
import com.atozcorporation.atoz.ui.login.LoginViewModel
import com.atozcorporation.atoz.ui.manageproduct.productlist.ProductListViewModel
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.base.OrderId
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel : BaseViewModel() {

    val productsListForPlaceOrder = MutableLiveData<List<Order_Summery_Ofline_Bean>>()
    val userDetailsOfOrderPlacer = MutableLiveData<LoginResponse.UserDetails>()
    val placeOrderAPIState = MutableLiveData<PlaceOrderAPIState>()

    sealed class PlaceOrderAPIState {
        object Loading : PlaceOrderAPIState()
        data class Success(val data: InsertResponse) : PlaceOrderAPIState()
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
                    userDetailsOfOrderPlacer.value?.contactNumber,
                    userDetailsOfOrderPlacer.value?.address1 + " \n" + userDetailsOfOrderPlacer.value?.address2,
                    userDetailsOfOrderPlacer.value?.name,
                    userDetailsOfOrderPlacer.value?.id.toString(),
                    productDetails.productUrl1,
                    "",
                    userDetailsOfOrderPlacer.value?.pinCode + ""
                )
                call.enqueue(object : Callback<InsertResponse> {
                    override fun onResponse(
                        call: Call<InsertResponse>,
                        response: Response<InsertResponse>
                    ) {
                        placeOrderAPIState.postValue(PlaceOrderAPIState.Success(response.body()))
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
                userDetailsOfOrderPlacer.value = response.body().data
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