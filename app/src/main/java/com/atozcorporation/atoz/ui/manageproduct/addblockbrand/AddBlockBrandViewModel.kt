package com.atozcorporation.atoz.ui.manageproduct.addblockbrand

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.spinnermaster.SpinnerMasterResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddBlockBrandViewModel : BaseViewModel() {
    val productBrandAPIState = MutableLiveData<ProductBrandAPIState>()
    val BrandList = MutableLiveData<SpinnerMasterResponse>()

    sealed class ProductBrandAPIState {
        object Loading : ProductBrandAPIState()
        data class Success(val data: SpinnerMasterResponse) : ProductBrandAPIState()
        data class Failure(val throwable: Throwable) : ProductBrandAPIState()
    }

    init {
        getProductBrandAPICall()
    }
    fun getProductBrandAPICall() {
        val call: Call<SpinnerMasterResponse> =
            apiService.Fill_SpinnerData("GetList", "productBrand", "*", "isActive = 1")
        call.enqueue(object : Callback<SpinnerMasterResponse> {
            override fun onResponse(
                call: Call<SpinnerMasterResponse>,
                response: Response<SpinnerMasterResponse>

            ) {
                BrandList.value = response.body()
            }

            override fun onFailure(
                call: Call<SpinnerMasterResponse>,
                t: Throwable
            ) {
                productBrandAPIState.postValue(ProductBrandAPIState.Failure(t))
            }
        })
    }
}