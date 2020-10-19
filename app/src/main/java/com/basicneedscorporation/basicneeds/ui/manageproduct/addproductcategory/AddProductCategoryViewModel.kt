package com.basicneedscorporation.basicneeds.ui.manageproduct.addproductcategory

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

class AddProductCategoryViewModel : BaseViewModel() {

    val addProductCategoryAPIState = MutableLiveData<AddProductCategoryAPIState>()

    sealed class AddProductCategoryAPIState {
        object Loading : AddProductCategoryAPIState()
        data class Success(val data: InsertResponse) : AddProductCategoryAPIState()
        data class Failure(val throwable: Throwable) : AddProductCategoryAPIState()
    }

    fun addProductCategoryAPICall(categoryName: String) {
        val imageFile = File(imageFilePath)
        val requestFile = RequestBody.create(MediaType.parse("image/png"), imageFile)
        var uploadedImage = MultipartBody.Part.createFormData(
            PROFILE_IMAGE,
            imageFile.name,
            requestFile
        )
        val categoryName: RequestBody = RequestBody.create(MultipartBody.FORM, categoryName)
        val methodName: RequestBody = RequestBody.create(MultipartBody.FORM, "Insert_Product_Category")
        addProductCategoryAPIState.postValue(AddProductCategoryAPIState.Loading)
        val call: Call<InsertResponse> =
            apiService.addNewProductCategory(methodName, categoryName, uploadedImage)
        call.enqueue(object : Callback<InsertResponse> {
            override fun onResponse(
                call: Call<InsertResponse>,
                response: Response<InsertResponse>

            ) {
                addProductCategoryAPIState.postValue(AddProductCategoryAPIState.Success(response.body()))
            }

            override fun onFailure(
                call: Call<InsertResponse>,
                t: Throwable
            ) {
                addProductCategoryAPIState.postValue(AddProductCategoryAPIState.Failure(t))
            }
        })
    }
}