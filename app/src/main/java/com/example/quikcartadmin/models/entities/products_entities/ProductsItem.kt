package com.example.quikcartadmin.models.entities.products_entities

import com.google.gson.annotations.SerializedName

data class ProductsItem(

	@field:SerializedName("image")
	val image: Image,

	@field:SerializedName("body_html")
	val bodyHtml: String,

	@field:SerializedName("images")
	val images: List<ImagesItem>,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("handle")
	val handle: String,

	@field:SerializedName("variants")
	val variants: List<VariantsItem>,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("tags")
	val tags: String,

	@field:SerializedName("published_scope")
	val publishedScope: String,

	@field:SerializedName("product_type")
	val productType: String,

	@field:SerializedName("template_suffix")
	val templateSuffix: Any,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("vendor")
	val vendor: String,

	@field:SerializedName("admin_graphql_api_id")
	val adminGraphqlApiId: String,

	@field:SerializedName("options")
	val options: List<OptionsItem>,

	@field:SerializedName("id")
	val id: Long,

	@field:SerializedName("published_at")
	val publishedAt: String,

	@field:SerializedName("status")
	val status: String
)