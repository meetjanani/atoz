package com.basicneedscorporation.basicneeds.rest.response.spinnermaster

data class SpinnerMasterResponse(
    val data: List<Data> = listOf(),
    val message: String = "",
    val status: Int = 0
) {
    data class Data(
        val id : Int = 0,
        val name : String = ""
    )
}