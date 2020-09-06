package com.atozcorporation.atoz.ui.manageproduct.productlist

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.product.ProductListResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListViewModel : BaseViewModel() {

    val productListAPIState = MutableLiveData<ProductListAPIState>()

    sealed class ProductListAPIState {
        object Loading : ProductListAPIState()
        data class Success(val data: ProductListResponse) : ProductListAPIState()
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
}