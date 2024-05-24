package com.example.quikcartadmin.models.entities.products

import com.google.gson.annotations.SerializedName

data class SingleProductsResponse(

	@field:SerializedName("product")
	val product: Product
)