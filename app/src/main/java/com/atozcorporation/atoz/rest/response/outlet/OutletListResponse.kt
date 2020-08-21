package com.atozcorporation.atoz.rest.response.outlet

data class OutletListResponse(
    val data: List<Outlet> = listOf(),
    val message: String = "",
    val status: Int = 0
) {
    data class Outlet(
        val address1: String = "",
        val address2: String = "",
        val contactNumber: String = "",
        val createdAt: String = "",
        val gst: String = "",
        val id: String = "",
        val name: String = "",
        val personName: String = "",
        val pinCode: String = "",
        val userId: String = "",
        val userName: String = "",
        val latitude: String = "",
        val longitude: String = "",
        val categoryId: Int = 0,
        val categoryName: String = "",
        val batchId: String = ""
    )
}