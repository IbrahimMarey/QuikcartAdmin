package com.example.quikcartadmin.models.entities.coupons

import com.google.gson.annotations.SerializedName

data class PriceRuleModel(
    @SerializedName("price_rules")
    var priceRules : ArrayList<PriceRule>
)
