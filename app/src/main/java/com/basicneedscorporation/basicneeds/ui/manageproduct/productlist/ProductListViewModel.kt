package com.basicneedscorporation.basicneeds.ui.manageproduct.productlist

import androidx.lifecycle.MutableLiveData
import com.basicneedscorporation.basicneeds.rest.response.product.ProductListResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListViewModel : BaseViewModel() {

    val productListAPIState = MutableLiveData<ProductListAPIState>()

    sealed class ProductListAPIState {
        object Loading : ProductListAPIState()
        data class Success(val data: ProductListResponse) : ProductListAPIState()
        data class SuccessDeleteProduct(val data: InsertResponse) : ProductListAPIState()
        data class Failure(val throwable: Throwable) : ProductListAPIState()
    }

    fun getProductListAPICall(productCategory : Int, productBrand : Int) {
        productListAPIState.postValue(ProductListAPIState.Loading)
        val call: Call<ProductListResponse> =
            apiService.getProductsList("GetList", "Product", "*", "productCategoryId = ${productCategory} and productBrandId = ${productBrand} and isActive = 1")
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

    fun deleteProductAPICall(productId : Int) {
        val call: Call<InsertResponse> =
            apiService.Common_Master_Update("Update", "Product", "`isActive` = '0'", "ID = '${productId}'")
        call.enqueue(object : Callback<InsertResponse> {
            override fun onResponse(
                call: Call<InsertResponse>,
                response: Response<InsertResponse>

            ) {
                productListAPIState.postValue(ProductListAPIState.SuccessDeleteProduct(response.body()))
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