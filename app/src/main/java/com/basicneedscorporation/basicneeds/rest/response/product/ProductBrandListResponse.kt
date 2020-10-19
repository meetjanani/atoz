package com.basicneedscorporation.basicneeds.rest.response.product

data class ProductBrandListResponse(
    val data: MutableList<ProductBrand> = mutableListOf(),
    val message: String = "",
    val status: Int = 0
) {
    data class ProductBrand(
        val createdAt: String = "",
        val id: Int = 0,
        val isActive: Int = 0,
        val name: String = "",
        val productCategoryId: Int = 0,
        val productCategoryName: String = "",
        val url1: String = ""
    )
}