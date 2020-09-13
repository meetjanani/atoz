package com.atozcorporation.atoz.ui.manageproduct.blockedbrandlist

import androidx.lifecycle.MutableLiveData
import com.atozcorporation.atoz.rest.response.login.LoginResponse
import com.atozcorporation.atoz.rest.response.product.ProductBrandListResponse
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.base.initWith
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlockedBrandListViewModel : BaseViewModel() {

    val blockedBrandAPIState = MutableLiveData<BlockedBrandAPIState>()
    val brandList = MutableLiveData<ProductBrandListResponse>()
    val bachId = MutableLiveData<String>()
    val blockedBrandList = MutableLiveData<ArrayList<Int>>().initWith(ArrayList())

    sealed class BlockedBrandAPIState {
        object Loading : BlockedBrandAPIState()
        data class SuccessUserDetails(val data: Boolean) : BlockedBrandAPIState()
        data class SuccessBlockedBrandList(val data: ArrayList<Int>) : BlockedBrandAPIState()
        data class Failure(val throwable: Throwable) : BlockedBrandAPIState()
    }

    init {
        getProductBrandAPICall()
    }
    fun getProductBrandAPICall() {
        blockedBrandAPIState.postValue(BlockedBrandAPIState.Loading)
        val call: Call<ProductBrandListResponse> =
            apiService.getProductBrand("GetList", "productBrand", "*", "isActive = 1")
        call.enqueue(object : Callback<ProductBrandListResponse> {
            override fun onResponse(
                call: Call<ProductBrandListResponse>,
                response: Response<ProductBrandListResponse>

            ) {
                brandList.value = response.body()
                getUserDetailsAPICall(0)
            }

            override fun onFailure(
                call: Call<ProductBrandListResponse>,
                t: Throwable
            ) {
                blockedBrandAPIState.postValue(BlockedBrandAPIState.Failure(t))
            }
        })
    }
    fun getUserDetailsAPICall(brandId : Int, isBlock : Boolean = false) {
        val call: Call<LoginResponse> =
            apiService.login("GetByID", "outlet", "*", "batchId = '${bachId.value.toString()}'")
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>

            ) {
                blockedBrandAPIState.postValue(BlockedBrandAPIState.SuccessUserDetails(true))
                if(brandId != 0){
                    if(isBlock){
                        blockedBrandList.value?.add(brandId)
                    } else {
                        blockedBrandList.value?.remove(brandId)
                    }
                    updateUserDetailsAPICall(blockedBrandList.value?.distinct()?.joinToString(",").toString())
                }
                if(response.body().data.blockBrand.length > 0){
                    blockedBrandList.value?.clear()
                   response.body().data.blockBrand.split(",").forEach {
                       blockedBrandList.value?.add(it.toInt())
                   }
                    blockedBrandList.value?.let {
                        blockedBrandAPIState.postValue(BlockedBrandAPIState.SuccessBlockedBrandList(it))
                    }
                }
            }

            override fun onFailure(
                call: Call<LoginResponse>,
                t: Throwable
            ) {
                //
            }
        })
    }

    fun updateUserDetailsAPICall(latestBlockedBrandList : String) {
        val call: Call<InsertResponse> =
            apiService.Common_Master_Update("Update", "outlet", "`blockBrand` = '${latestBlockedBrandList}'", "batchId = '${bachId.value.toString()}'")
        call.enqueue(object : Callback<InsertResponse> {
            override fun onResponse(
                call: Call<InsertResponse>,
                response: Response<InsertResponse>

            ) {
                getUserDetailsAPICall(0)
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