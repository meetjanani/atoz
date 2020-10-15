package com.atozcorporation.atoz.rest.response.product

data class ProductListResponse(
    val status: Int,
    val message: String,
    val data: List<ProductDetails>
) {
    data class ProductDetails(
        val ID: Int,
        val Name: String,
        val Pack_Size: String,
        val MRP_1: Double,
        val MRP_2: Double,
        val Min_Qty: Int,
        val Brand_Name: String,
        val productCategoryId: Int,
        val productCategoryName: String,
        val productBrandId: Int,
        val productBrandName: String,
        val Seq: Int,
        val Description: String,
        val productCode: String,
        val Created_At: String,
        val isActive: Int,
        val URL_1: String,
        val Amazon_Link: String,
        val FlipKart_Link: String,
        val Avaiable_Stock: Int,
        val Sold_Stock: Int,
        val Total_Stock: Int
    )
}