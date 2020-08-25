package com.growinginfotech.businesshub.rest

import com.atozcorporation.atoz.rest.response.login.LoginResponse
import com.atozcorporation.atoz.rest.response.outlet.OutletCategoryResponse
import com.atozcorporation.atoz.rest.response.outlet.OutletListResponse
import com.atozcorporation.atoz.rest.response.product.ProductCategoryResponse
import com.atozcorporation.atoz.rest.response.spinnermaster.SpinnerMasterResponse
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    // Display Patch
    @FormUrlEncoded
    @POST("DynamicQuery.php")
    fun login(
        @Field("methodname") methodname: String,
        @Field("TableName") TableName: String,
        @Field("Col") Col: String,
        @Field("WhereClouse") WhereClouse: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("DynamicQuery.php")
    fun getOutletList(
        @Field("methodname") methodname: String,
        @Field("TableName") TableName: String,
        @Field("Col") Col: String,
        @Field("WhereClouse") WhereClouse: String
    ): Call<OutletListResponse>

    @FormUrlEncoded
    @POST("DynamicQuery.php")
    fun getOutletCategory(
        @Field("methodname") methodname: String,
        @Field("TableName") TableName: String,
        @Field("Col") Col: String,
        @Field("WhereClouse") WhereClouse: String
    ): Call<OutletCategoryResponse>

    @FormUrlEncoded
    @POST("DynamicQuery.php")
    fun Common_Master_Insert(
        @Field("methodname") methodname: String?,
        @Field("TableName") TableName: String?,
        @Field("Col") Col: String?,
        @Field("ColData") ColData: String?
    ): Call<InsertResponse>

    @FormUrlEncoded
    @POST("DynamicQuery.php")
    fun Common_Master_Update(
        @Field("methodname") methodname: String,
        @Field("TableName") TableName: String,
        @Field("ColAndData") Col: String,
        @Field("WhereClouse") WhereClouse: String
    ): Call<InsertResponse>

    // Fill Spinner Data
    @FormUrlEncoded
    @POST("DynamicQuery.php")
    fun Fill_SpinnerData(
        @Field("methodname") methodname: String?,
        @Field("TableName") TableName: String?,
        @Field("Col") Col: String?,
        @Field("WhereClouse") WhereClouse: String?
    ): Call<SpinnerMasterResponse>

    @FormUrlEncoded
    @POST("DynamicQuery.php")
    fun getProductCategory(
        @Field("methodname") methodname: String,
        @Field("TableName") TableName: String,
        @Field("Col") Col: String,
        @Field("WhereClouse") WhereClouse: String
    ): Call<ProductCategoryResponse>

    @Multipart
    @POST("master.php   ")
    fun addNewProductCategory(
        @Part("methodname") methodname: RequestBody,
        @Part("name") name: RequestBody,
        @Part imageFile: MultipartBody.Part?
    ): Call<InsertResponse>

    @Multipart
    @POST("master.php   ")
    fun addNewProductBrand(
        @Part("methodname") methodname: RequestBody,
        @Part("name") name: RequestBody,
        @Part imageFile: MultipartBody.Part?
    ): Call<InsertResponse>
}