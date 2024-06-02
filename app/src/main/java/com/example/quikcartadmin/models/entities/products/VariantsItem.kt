package com.example.quikcartadmin.models.entities.products

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class VariantsItem(
	val option1: String?,
	val option3: String?,
	val option2: String?,
	val price: String?,
	val id: Long?,
	val product_id: Long?,
	val title: String?,
	val sku: String?,
	val position: Int?,
	val inventory_policy: String?,
	val compare_at_price: String?,
	val fulfillment_service: String?,
	val inventory_management: String?,
	val created_at: String?,
	val updated_at: String?,
	val taxable: Boolean?,
	val barcode: String?,
	val grams: Int?,

	val weight: Double?,
	val weight_unit: String?,
	val inventory_item_id: Long?,
	val inventory_quantity: Int?,
	val old_inventory_quantity: Int?,
	val requires_shipping: Boolean?,
	val admin_graphql_api_id: String?,
	val image_id: Long?
)
