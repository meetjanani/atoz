package com.atozcorporation.atoz.ui.manageproduct.addproductbrand

import androidx.lifecycle.MutableLiveData
import com.growinginfotech.businesshub.base.BaseViewModel
import com.growinginfotech.businesshub.base.PROFILE_IMAGE
import com.growinginfotech.businesshub.base.imageFilePath
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddProductBrandViewModel : BaseViewModel() {

    val addProductBrandAPIState = MutableLiveData<AddProductBrandAPIState>()

    sealed class AddProductBrandAPIState {
        object Loading : AddProductBrandAPIState()
        data class Success(val data: InsertResponse) : AddProductBrandAPIState()
        data class Failure(val throwable: Throwable) : AddProductBrandAPIState()
    }

    fun addProductBrandAPICall(categoryName: String) {
        val imageFile = File(imageFilePath)
        val requestFile = RequestBody.create(MediaType.parse("image/png"), imageFile)
        var uploadedImage = MultipartBody.Part.createFormData(
            PROFILE_IMAGE,
            imageFile.name,
            requestFile
        )
        val categoryName: RequestBody = RequestBody.create(MultipartBody.FORM, categoryName)
        val methodName: RequestBody = RequestBody.create(MultipartBody.FORM, "Insert_Product_Brand")
        addProductBrandAPIState.postValue(AddProductBrandAPIState.Loading)
        val call: Call<InsertResponse> =
            apiService.addNewProductBrand(methodName, categoryName, uploadedImage)
        call.enqueue(object : Callback<InsertResponse> {
            override fun onResponse(
                call: Call<InsertResponse>,
                response: Response<InsertResponse>

            ) {
                addProductBrandAPIState.postValue(AddProductBrandAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<InsertResponse>,
                t: Throwable
            ) {
                addProductBrandAPIState.postValue(AddProductBrandAPIState.Failure(t))
            }
        })
    }
}