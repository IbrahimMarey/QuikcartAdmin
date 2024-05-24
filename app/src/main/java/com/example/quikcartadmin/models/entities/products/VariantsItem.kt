package com.example.quikcartadmin.models.entities.products

import com.google.gson.annotations.SerializedName

data class VariantsItem(

	@field:SerializedName("inventory_management")
	val inventoryManagement: String,

	@field:SerializedName("old_inventory_quantity")
	val oldInventoryQuantity: Int,

	@field:SerializedName("requires_shipping")
	val requiresShipping: Boolean,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("inventory_item_id")
	val inventoryItemId: Long,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("product_id")
	val productId: Long,

	@field:SerializedName("option3")
	val option3: Any,

	@field:SerializedName("option1")
	val option1: String,

	@field:SerializedName("id")
	val id: Long,

	@field:SerializedName("option2")
	val option2: String,

	@field:SerializedName("sku")
	val sku: String,

	@field:SerializedName("grams")
	val grams: Int,

	@field:SerializedName("barcode")
	val barcode: Any,

	@field:SerializedName("inventory_quantity")
	val inventoryQuantity: Int,

	@field:SerializedName("compare_at_price")
	val compareAtPrice: Any,

	@field:SerializedName("fulfillment_service")
	val fulfillmentService: String,

	@field:SerializedName("taxable")
	val taxable: Boolean,

	@field:SerializedName("weight")
	val weight: Any,

	@field:SerializedName("inventory_policy")
	val inventoryPolicy: String,

	@field:SerializedName("weight_unit")
	val weightUnit: String,

	@field:SerializedName("admin_graphql_api_id")
	val adminGraphqlApiId: String,

	@field:SerializedName("position")
	val position: Int,

	@field:SerializedName("image_id")
	val imageId: Any
)