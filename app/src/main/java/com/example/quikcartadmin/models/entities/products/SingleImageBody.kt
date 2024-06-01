package com.example.quikcartadmin.models.entities.products

import com.google.gson.annotations.SerializedName

data class SingleImageBody(

	@field:SerializedName("image")
	val image: SingleImage? = null
)

data class SingleImage(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("src")
	val src: String? = null,

	@field:SerializedName("product_id")
	val productId: Long? = null,

	@field:SerializedName("admin_graphql_api_id")
	val adminGraphqlApiId: Any? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("alt")
	val alt: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("variant_ids")
	val variantIds: List<Any?>? = null,

	@field:SerializedName("position")
	val position: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("height")
	val height: Int? = null
)
