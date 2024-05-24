package com.example.quikcartadmin.models.entities.products

import com.google.gson.annotations.SerializedName

data class ProductBody(

	@field:SerializedName("product")
	val product: Product
)