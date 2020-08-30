package com.atozcorporation.atoz.ui.manageproduct.productbrandlist

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.product.ProductBrandListResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductBrandListViewModel : BaseViewModel() {

    val productBrandAPIState = MutableLiveData<ProductBrandAPIState>()

    sealed class ProductBrandAPIState {
        object Loading : ProductBrandAPIState()
        data class Success(val data: ProductBrandListResponse) : ProductBrandAPIState()
        data class Failure(val throwable: Throwable) : ProductBrandAPIState()
    }

    fun getProductBrandAPICall(productCategory : Int) {
        productBrandAPIState.postValue(ProductBrandAPIState.Loading)
        val call: Call<ProductBrandListResponse> =
            apiService.getProductBrand("GetList", "productBrand", "*", "productCategoryId = ${productCategory} and isActive = 1")
        call.enqueue(object : Callback<ProductBrandListResponse> {
            override fun onResponse(
                call: Call<ProductBrandListResponse>,
                response: Response<ProductBrandListResponse>

            ) {
                productBrandAPIState.postValue(ProductBrandAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<ProductBrandListResponse>,
                t: Throwable
            ) {
                productBrandAPIState.postValue(ProductBrandAPIState.Failure(t))
            }
        })
    }
}