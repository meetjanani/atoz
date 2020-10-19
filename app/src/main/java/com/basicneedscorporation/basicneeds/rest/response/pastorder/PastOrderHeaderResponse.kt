package com.basicneedscorporation.basicneeds.rest.response.pastorder

data class PastOrderHeaderResponse(
    val status: Int,
    val message: String,
    val data: MutableList<PastOrderHeaderDetails> = mutableListOf()
) {
    data class PastOrderHeaderDetails(
        val Order_ID: String,
        val Order_Status: String,
        val Order_Total: String,
        val Product_Count: String,
        val Product_Qty: String,
        val orderById: String,
        val orderByName: String,
        val orderByBatchId: String,
        val orderForId: String,
        val orderForName: String,
        val orderForBatchId: String,
        val Order_Type: String,
        val Created_Date: String,
        val URL_1: String
    )
}