package com.example.quikcartadmin.models.entities.products

import com.google.gson.annotations.SerializedName

data class ProductsResponse(

	@field:SerializedName("products")
	val products: List<ProductsItem>
)