package com.atozcorporation.atoz.ui.manageproduct.productbrandlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.login.LoginResponse
import com.atozcorporation.atoz.rest.response.product.ProductBrandListResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductBrandListViewModel : BaseViewModel() {

    val productBrandAPIState = MutableLiveData<ProductBrandAPIState>()
    val productCategory = MutableLiveData<Int>()

    sealed class ProductBrandAPIState {
        object Loading : ProductBrandAPIState()
        data class Success(val data: MutableList<ProductBrandListResponse.ProductBrand>) : ProductBrandAPIState()
        data class Failure(val throwable: Throwable) : ProductBrandAPIState()
    }

    fun getUserDetailsAPICall(batch : String) {
        val call: Call<LoginResponse> =
            apiService.login("GetByID", "outlet", "*", "batchId = '${batch}' OR contactNumber = '${batch}'")
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>

            ) {
                getProductBrandAPICall(productCategory.value ?: 0, response.body().data.blockBrand )
            }

            override fun onFailure(
                call: Call<LoginResponse>,
                t: Throwable
            ) {
                //
            }
        })
    }

    fun getProductBrandAPICall(productCategory : Int, blockedBrand : String) {
        productBrandAPIState.postValue(ProductBrandAPIState.Loading)
        val call: Call<ProductBrandListResponse> =
            apiService.getProductBrand("GetList", "productBrand", "*", "productCategoryId = ${productCategory} and isActive = 1")
        call.enqueue(object : Callback<ProductBrandListResponse> {
            override fun onResponse(
                call: Call<ProductBrandListResponse>,
                response: Response<ProductBrandListResponse>

            ) {
                val brandList = response.body().data
                if(blockedBrand.length > 0){
                    blockedBrand.split(",").forEach {blockedBrandId ->
                        brandList.remove(response.body().data.singleOrNull { it.id == blockedBrandId.toInt() })
                    }
                }
                productBrandAPIState.postValue(ProductBrandAPIState.Success(brandList))
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
// SELECT * FROM `productBrand` WHERE id Not in (SELECT blockBrand from outlet where batchId = 'GS2501')