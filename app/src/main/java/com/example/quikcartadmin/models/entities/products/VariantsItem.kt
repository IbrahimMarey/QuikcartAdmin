package com.example.quikcartadmin.models.entities.products

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class VariantsItem(
	val option1: String? = null,
	val option3: String?= null,
	val option2: String?= null,
	val price: String?= null,
	val id: Long?= null,
	val product_id: Long?= null,
	val title: String?= null,
	val sku: String?= null,
	val position: Int?= null,
	val inventory_policy: String?= null,
	val compare_at_price: String?= null,
	val fulfillment_service: String?= null,
	val inventory_management: String?= null,
	val created_at: String?= null,
	val updated_at: String?= null,
	val taxable: Boolean?= null,
	val barcode: String?= null,
	val grams: Int?= null,

	val weight: Double?= null,
	val weight_unit: String?= null,
	val inventory_item_id: Long?= null,
	val inventory_quantity: Int?= null,
	val old_inventory_quantity: Int?= null,
	val requires_shipping: Boolean?= null,
	val admin_graphql_api_id: String?= null,
	val image_id: Long?= null
)
