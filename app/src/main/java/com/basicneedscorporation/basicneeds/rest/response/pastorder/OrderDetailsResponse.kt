package com.basicneedscorporation.basicneeds.rest.response.pastorder

data class OrderDetailsResponse(
    val status: Int,
    val message: String,
    val data: MutableList<OrderDetails> = mutableListOf()
) {
    data class OrderDetails(
        val ID: String,
        val Order_ID: String,
        val Order_Status: String,
        val Order_Type: Int,
        val Product_ID: String,
        val Product_Name: String,
        val ProductCategoryId: String,
        val Product_Category: String,
        val ProductBrandId: String,
        val ProductBrandName: String,
        val Qty: String,
        val Product_Price: String,
        val Product_Total: String,
        val Order_Total: String,
        val User_Mobile_No: String,
        val Address_1: String,
        val Pincode: String,
        val orderByName: String,
        val orderById: String,
        val orderByBatchId: String,
        val orderForName: String,
        val orderForId: String,
        val orderForBatchId: String,
        val URL_1: String,
        val comments: String,
        val Created_Date: String,
        val Is_Active: String
    )
}