package com.example.quikcartadmin.models.entities.products

import com.google.gson.annotations.SerializedName

data class PresentmentPricesItem(

	@field:SerializedName("compare_at_price")
	val compareAtPrice: Any,

	@field:SerializedName("price")
	val price: Price
)