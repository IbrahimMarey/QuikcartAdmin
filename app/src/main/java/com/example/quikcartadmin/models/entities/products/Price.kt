package com.example.quikcartadmin.models.entities.products

import com.google.gson.annotations.SerializedName

data class Price(

	@field:SerializedName("amount")
	val amount: String,

	@field:SerializedName("currency_code")
	val currencyCode: String
)