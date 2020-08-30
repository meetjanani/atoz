package com.atozcorporation.atoz.rest.response.outlet

data class OutletDetailsResponse(
    val data: Data = Data(),
    val message: String = "",
    val status: Int = 0
) {
    data class Data(
        val address1: String = "",
        val address2: String = "",
        val areaId: String = "",
        val areaName: String = "",
        val batchId: String = "",
        val categoryId: String = "",
        val categoryName: String = "",
        val cityId: String = "",
        val cityName: String = "",
        val contactNumber: String = "",
        val createdAt: String = "",
        val gst: String = "",
        val id: String = "",
        val isActive: String = "",
        val latitude: String = "",
        val longitude: String = "",
        val name: String = "",
        val outletOnId: String = "",
        val outletOnName: String = "",
        val outletSince: String = "",
        val password: String = "",
        val personName: String = "",
        val pinCode: String = "",
        val rollId: String = "",
        val userId: String = "",
        val userName: String = ""
    )
}