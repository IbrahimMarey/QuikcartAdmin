package com.example.quikcartadmin.ui.coupons.creatediscount.view

import com.example.quikcartadmin.models.entities.coupons.DiscountCode

interface DiscountListener {

    fun getDiscounts()
    fun updateDiscount(discountCode: DiscountCode)

    fun deleteDiscount(discountCode: DiscountCode)
}