package com.atozcorporation.atoz.rest.response.login

data class LoginResponse(
    val data: UserDetails = UserDetails(),
    val message: String = "",
    val status: Int = 0
) {
    data class UserDetails(
        val address1: String = "",
        val address2: String = "",
        val areaId: Int = 0,
        val areaName: String = "",
        var batchId: String = "",
        val categoryId: Int = 0,
        val categoryName: String = "",
        val cityId: Int = 0,
        val cityName: String = "",
        var contactNumber: String = "",
        val createdAt: String = "",
        val gst: String = "",
        var id: Int = 0,
        val isActive: Int = 0,
        val latitude: String = "",
        val longitude: String = "",
        var name: String = "",
        val password: String = "",
        var personName: String = "",
        val pinCode: String = "",
        val rollId: Int = 0,
        val userId: Int = 0,
        val userName: String = "",
        val outletOnId: Int = 0,
        val outletOnName: String = "",
        val outletSince: String = "",
        val aadharCard: String = "",
        val panCard: String = "",
        val other1: String = ""
    )
}