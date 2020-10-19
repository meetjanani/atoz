package com.basicneedscorporation.basicneeds.rest.response.outlet

data class OutletCategoryResponse(
    val data: List<Data> = listOf(),
    val message: String = "",
    val status: Int = 0
) {
    data class Data(
        val createdAt: String = "",
        val id: Int = 0,
        val isActive: String = "",
        val name: String = "",
        val url1: String = ""
    )
}