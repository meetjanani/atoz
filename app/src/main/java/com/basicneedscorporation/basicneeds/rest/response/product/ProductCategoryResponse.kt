package com.basicneedscorporation.basicneeds.rest.response.product

data class ProductCategoryResponse(
    val data: List<ProductCategory> = listOf(),
    val message: String = "",
    val status: Int = 0
) {
    data class ProductCategory(
        val createdAt: String = "",
        val id: Int = 0,
        val isActive: Int = 0,
        val name: String = "",
        val url1: String = ""
    )
}