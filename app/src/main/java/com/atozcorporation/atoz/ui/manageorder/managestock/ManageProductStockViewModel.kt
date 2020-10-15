package com.atozcorporation.atoz.ui.manageorder.managestock

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.product.ProductListResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageProductStockViewModel : BaseViewModel() {
    val productListAPIState = MutableLiveData<ProductListAPIState>()

    sealed class ProductListAPIState {
        object Loading : ProductListAPIState()
        data class Success(val data: ProductListResponse) : ProductListAPIState()
        data class SuccessStockUpdated(val data: InsertResponse) : ProductListAPIState()
        data class Failure(val throwable: Throwable) : ProductListAPIState()
    }

    fun getProductListAPICall(productID : String) {
        productListAPIState.postValue(ProductListAPIState.Loading)
        val call: Call<ProductListResponse> =
            apiService.getProductsList("GetList", "Product", "*", "ID = ${productID}")
        call.enqueue(object : Callback<ProductListResponse> {
            override fun onResponse(
                call: Call<ProductListResponse>,
                response: Response<ProductListResponse>

            ) {
                productListAPIState.postValue(ProductListAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<ProductListResponse>,
                t: Throwable
            ) {
                productListAPIState.postValue(ProductListAPIState.Failure(t))
            }
        })
    }

    fun manageProductStockAPICall(productID : String, availableStock : String, totalStock : String) {
        productListAPIState.postValue(ProductListAPIState.Loading)
        val call: Call<InsertResponse> =
            apiService.manageProductStockByProductId("Manage_Product_Stock", availableStock, totalStock, productID)
        call.enqueue(object : Callback<InsertResponse> {
            override fun onResponse(
                call: Call<InsertResponse>,
                response: Response<InsertResponse>

            ) {
                productListAPIState.postValue(ProductListAPIState.SuccessStockUpdated(response.body()))
            }

            override fun onFailure(
                call: Call<InsertResponse>,
                t: Throwable
            ) {
                productListAPIState.postValue(ProductListAPIState.Failure(t))
            }
        })
    }
}