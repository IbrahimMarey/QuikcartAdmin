package com.example.quikcartadmin.models.entities.coupons

import java.io.Serializable


data class PriceRule(
    val id: Long? = null,
    val allocation_limit: String? = null,
    val allocation_method: String? = null,
    val created_at: String? = null,
    val customer_selection: String? = null,
    var ends_at: String? = null,
    val once_per_customer: Boolean? = null,
    var starts_at: String? = null,
    val target_selection: String? = null,
    val target_type: String? = null,
    var title: String? = null,
    val updated_at: String? = null,
    val usage_limit: String? = null,
    var value: String? = null,
    var value_type: String? = null,
) : Serializable