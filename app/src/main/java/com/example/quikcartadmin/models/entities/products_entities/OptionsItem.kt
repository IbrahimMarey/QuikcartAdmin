package com.example.quikcartadmin.models.entities.products_entities

import com.google.gson.annotations.SerializedName

data class OptionsItem(

	@field:SerializedName("product_id")
	val productId: Long,

	@field:SerializedName("values")
	val values: List<String>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Long,

	@field:SerializedName("position")
	val position: Int
)