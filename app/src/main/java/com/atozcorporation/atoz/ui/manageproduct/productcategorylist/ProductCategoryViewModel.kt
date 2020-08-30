package com.atozcorporation.atoz.ui.manageproduct.productcategorylist

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.product.ProductCategoryResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductCategoryViewModel : BaseViewModel() {

    val productCategoryAPIState = MutableLiveData<ProductCategoryAPIState>()

    sealed class ProductCategoryAPIState {
        object Loading : ProductCategoryAPIState()
        data class Success(val data: ProductCategoryResponse) : ProductCategoryAPIState()
        data class Failure(val throwable: Throwable) : ProductCategoryAPIState()
    }
    init {
        getProductCategoryAPICall()
    }

    fun getProductCategoryAPICall() {
        productCategoryAPIState.postValue(ProductCategoryAPIState.Loading)
        val call: Call<ProductCategoryResponse> =
            apiService.getProductCategory("GetList", "productCategory", "*", "isActive = 1")
        call.enqueue(object : Callback<ProductCategoryResponse> {
            override fun onResponse(
                call: Call<ProductCategoryResponse>,
                response: Response<ProductCategoryResponse>

            ) {
                productCategoryAPIState.postValue(ProductCategoryAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<ProductCategoryResponse>,
                t: Throwable
            ) {
                productCategoryAPIState.postValue(ProductCategoryAPIState.Failure(t))
            }
        })
    }
}