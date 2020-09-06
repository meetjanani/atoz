package com.atozcorporation.atoz.ui.manageproduct.addproduct

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

class AddProductsViewModel : BaseViewModel() {

    val addProductsAPIState = MutableLiveData<AddProductsAPIState>()

    sealed class AddProductsAPIState {
        object Loading : AddProductsAPIState()
        data class Success(val data: InsertResponse) : AddProductsAPIState()
        data class Failure(val throwable: Throwable) : AddProductsAPIState()
    }

    fun addProductsAPICall(brandName: String, categoryId: Int, categoryName: String) {
        val imageFile = File(imageFilePath)
        val requestFile = RequestBody.create(MediaType.parse("image/png"), imageFile)
        var uploadedImage = MultipartBody.Part.createFormData(
            PROFILE_IMAGE,
            imageFile.name,
            requestFile
        )
        val brandName: RequestBody = RequestBody.create(MultipartBody.FORM, brandName)
        val categoryId: RequestBody = RequestBody.create(MultipartBody.FORM, categoryId.toString())
        val categoryName: RequestBody = RequestBody.create(MultipartBody.FORM, categoryName)
        val methodName: RequestBody = RequestBody.create(MultipartBody.FORM, "Insert_Product")
        addProductsAPIState.postValue(AddProductsAPIState.Loading)
        val call: Call<InsertResponse> =
            apiService.addNewProducts(methodName, brandName,categoryId, categoryName, uploadedImage)
        call.enqueue(object : Callback<InsertResponse> {
            override fun onResponse(
                call: Call<InsertResponse>,
                response: Response<InsertResponse>

            ) {
                addProductsAPIState.postValue(AddProductsAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<InsertResponse>,
                t: Throwable
            ) {
                addProductsAPIState.postValue(AddProductsAPIState.Failure(t))
            }
        })
    }
}