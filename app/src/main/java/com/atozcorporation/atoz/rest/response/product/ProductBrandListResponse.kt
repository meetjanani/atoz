package com.atozcorporation.atoz.rest.response.product

data class ProductBrandListResponse(
    val data: List<ProductBrand> = listOf(),
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