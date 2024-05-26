package com.example.quikcartadmin.models.entities.products

import com.google.gson.annotations.SerializedName

data class ImagesItem(

	@field:SerializedName("updated_at")
	val updatedAt: String?,

	@field:SerializedName("src")
	val src: String?,

	@field:SerializedName("product_id")
	val productId: Long?,

	@field:SerializedName("admin_graphql_api_id")
	val adminGraphqlApiId: String?,

	@field:SerializedName("alt")
	val alt: Any?,

	@field:SerializedName("width")
	val width: Int?,

	@field:SerializedName("created_at")
	val createdAt: String?,

	@field:SerializedName("variant_ids")
	val variantIds: List<Any>?,

	@field:SerializedName("id")
	val id: Long?,

	@field:SerializedName("position")
	val position: Int?,

	@field:SerializedName("height")
	val height: Int?
)