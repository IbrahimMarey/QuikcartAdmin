package com.example.quikcartadmin.models.entities.products

import com.google.gson.annotations.SerializedName

data class ProductsCountResponse(

	@field:SerializedName("count")
	val count: Int? = null
)
