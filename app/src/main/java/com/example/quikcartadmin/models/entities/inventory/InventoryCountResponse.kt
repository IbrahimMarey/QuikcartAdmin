package com.example.quikcartadmin.models.entities.inventory

import com.google.gson.annotations.SerializedName

data class InventoryCountResponse(

	@field:SerializedName("count")
	val count: Int? = null
)