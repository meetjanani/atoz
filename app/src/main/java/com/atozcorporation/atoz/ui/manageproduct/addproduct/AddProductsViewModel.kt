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

    fun addProductsAPICall(name: String,packSize: String,mrp1: String,mrp2: String,mimQty: String, categoryId: Int, categoryName: String, brandId: Int,brandName: String, description : String, productCode : String) {
        val imageFile = File(imageFilePath)
        val requestFile = RequestBody.create(MediaType.parse("image/png"), imageFile)
        var uploadedImage = MultipartBody.Part.createFormData(
            PROFILE_IMAGE,
            imageFile.name,
            requestFile
        )
        val name: RequestBody = RequestBody.create(MultipartBody.FORM, name)
        val packSize: RequestBody = RequestBody.create(MultipartBody.FORM, packSize)
        val mrp1: RequestBody = RequestBody.create(MultipartBody.FORM, mrp1)
        val mrp2: RequestBody = RequestBody.create(MultipartBody.FORM, mrp2)
        val mimQty: RequestBody = RequestBody.create(MultipartBody.FORM, mimQty)
        val categoryId: RequestBody = RequestBody.create(MultipartBody.FORM, categoryId.toString())
        val categoryName: RequestBody = RequestBody.create(MultipartBody.FORM, categoryName)
        val brandId: RequestBody = RequestBody.create(MultipartBody.FORM, brandId.toString())
        val brandName: RequestBody = RequestBody.create(MultipartBody.FORM, brandName)
        val description: RequestBody = RequestBody.create(MultipartBody.FORM, description)
        val productCode: RequestBody = RequestBody.create(MultipartBody.FORM, productCode)
        val methodName: RequestBody = RequestBody.create(MultipartBody.FORM, "Insert_New_Product")
        addProductsAPIState.postValue(AddProductsAPIState.Loading)
        val call: Call<InsertResponse> =
            apiService.addNewProducts(methodName, name, packSize, mrp1, mrp2,mimQty,categoryId, categoryName,brandId,brandName,description, productCode, uploadedImage)
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