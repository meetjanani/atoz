package com.atozcorporation.atoz.rest.response.outlet

import java.io.Serializable

data class OutletListResponse(
    val `data`: List<Outlet> = listOf(),
    val message: String = "",
    val status: Int = 0
) {
    data class Outlet(
        val address1: String = "",
        val address2: String = "",
        val areaId: Int = 0,
        val areaName: String = "",
        val batchId: String = "",
        val categoryId: Int = 0,
        val categoryName: String = "",
        val cityId: Int = 0,
        val cityName: String = "",
        val contactNumber: String = "",
        val createdAt: String = "",
        val gst: String = "",
        val id: Int = 0,
        val isActive: Int = 0,
        val latitude: String = "",
        val longitude: String = "",
        val name: String = "",
        val password: String = "",
        val personName: String = "",
        val pinCode: String = "",
        val rollId: Int = 0,
        val userId: Int = 0,
        val userName: String = ""
    ) : Serializable
}