package com.example.quikcartadmin.models.entities.coupons

import com.google.gson.annotations.SerializedName

data class DiscountCodeBody(
    @SerializedName("discount_code")
    var discountCode : SingleDiscountCode
)

data class SingleDiscountCode(

    @SerializedName("code")
    var code : String
)