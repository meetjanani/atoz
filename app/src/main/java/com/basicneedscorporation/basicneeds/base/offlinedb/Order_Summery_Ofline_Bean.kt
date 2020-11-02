package com.basicneedscorporation.basicneeds.base.offlinedb

data class Order_Summery_Ofline_Bean(
    var productId : String = "",
    var productName : String = "",
    var productQty : String = "",
    var productPrice : String = "",
    var productTotal : String = "",
    var productCategoryId : String = "",
    var productCategoryName : String = "",
    var productBrandId : String = "",
    var productBrandName : String = "",
    var productUrl1 : String = "",
    var orderTotal : String = "",
    var productCode : String = "",
    var Pack_Size : String = "",
    var Min_Qty : String = "",
    var PcsInUnit : String = ""
)