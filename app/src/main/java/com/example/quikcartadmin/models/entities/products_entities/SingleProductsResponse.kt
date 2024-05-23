package com.example.quikcartadmin.models.entities.products_entities

import com.google.gson.annotations.SerializedName

data class SingleProductsResponse(

	@field:SerializedName("product")
	val product: Product
)