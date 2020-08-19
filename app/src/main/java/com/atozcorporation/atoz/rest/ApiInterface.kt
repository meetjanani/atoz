package com.growinginfotech.businesshub.rest

import com.atozcorporation.atoz.rest.response.outlet.OutletCategoryResponse
import com.atozcorporation.atoz.rest.response.outlet.OutletListResponse
import com.growinginfotech.businesshub.rest.response.insert.InsertResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    // Display Patch
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
}