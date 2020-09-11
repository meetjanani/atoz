package com.growinginfotech.businesshub.rest

import com.atozcorporation.atoz.rest.response.login.LoginResponse
import com.atozcorporation.atoz.rest.response.outlet.OutletCategoryResponse
import com.atozcorporation.atoz.rest.response.outlet.OutletDetailsResponse
import com.atozcorporation.atoz.rest.response.outlet.OutletListResponse
import com.atozcorporation.atoz.rest.response.product.ProductBrandListResponse
import com.atozcorporation.atoz.rest.response.product.ProductCategoryResponse
import com.atozcorporation.atoz.rest.response.product.ProductListResponse
import com.atozcorporation.atoz.rest.response.spinnermaster.SpinnerMasterResponse
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
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

    @FormUrlEncoded
    @POST("DynamicQuery.php")
    fun getProductBrand(
        @Field("methodname") methodname: String,
        @Field("TableName") TableName: String,
        @Field("Col") Col: String,
        @Field("WhereClouse") WhereClouse: String
    ): Call<ProductBrandListResponse>

    @FormUrlEncoded
    @POST("DynamicQuery.php")
    fun getProductsList(
        @Field("methodname") methodname: String,
        @Field("TableName") TableName: String,
        @Field("Col") Col: String,
        @Field("WhereClouse") WhereClouse: String
    ): Call<ProductListResponse>

    @Multipart
    @POST("master.php")
    fun addNewProductCategory(
        @Part("methodname") methodname: RequestBody,
        @Part("name") name: RequestBody,
        @Part imageFile: MultipartBody.Part?
    ): Call<InsertResponse>

    @Multipart
    @POST("master.php")
    fun addNewProductBrand(
        @Part("methodname") methodname: RequestBody,
        @Part("name") name: RequestBody,
        @Part("productCategoryId") productCategoryId: RequestBody,
        @Part("productCategoryName") productCategoryName: RequestBody,
        @Part imageFile: MultipartBody.Part?
    ): Call<InsertResponse>

    @Multipart
    @POST("master.php")
    fun addNewProducts(
        @Part("methodname") methodname: RequestBody,
        @Part("name") name: RequestBody,
        @Part("productCategoryId") productCategoryId: RequestBody,
        @Part("productCategoryName") productCategoryName: RequestBody,
        @Part imageFile: MultipartBody.Part?
    ): Call<InsertResponse>

    @FormUrlEncoded
    @POST("DynamicQuery.php")
    fun verifyOutletDetails(
        @Field("methodname") methodname: String,
        @Field("TableName") TableName: String,
        @Field("Col") Col: String,
        @Field("WhereClouse") WhereClouse: String
    ): Call<OutletDetailsResponse>

    @FormUrlEncoded
    @POST("Shooping_Order.php")
    fun Fetch_New_Order_ID(
        @Field("methodname") methodname: String?,
        @Field("ID") ID: String?
    ): Call<InsertResponse>

    // Insert New Order
    @FormUrlEncoded
    @POST("Shooping_Order.php")
    fun Add_New_Order(
        @Field("methodname") methodname: String?,
        @Field("Order_ID") Order_ID: String?,
        @Field("Order_Status") Order_Status: String?,
        @Field("Order_Type") Order_Type: String?,
        @Field("Product_ID") Product_ID: String?,
        @Field("Product_Name") Product_Name: String?,
        @Field("ProductCategoryId") ProductCategoryId: String?,
        @Field("Product_Category") Product_Category: String?,
        @Field("ProductBrandId") ProductBrandId: String?,
        @Field("ProductBrandName") ProductBrandName: String?,
        @Field("Qty") Qty: String?,
        @Field("Product_Price") Product_Price: String?,
        @Field("Product_Total") Product_Total: String?,
        @Field("Order_Total") Order_Total: String?,
        @Field("User_Mobile_No") User_Mobile_No: String?,
        @Field("Address_1") Address_1: String?,
        @Field("User_Name") User_Name: String?,
        @Field("User_ID") User_ID: String?,
        @Field("URL_1") URL_1: String?,
        @Field("comments") comments: String?,
        @Field("Pincode") Pincode: String?
    ): Call<InsertResponse>
}