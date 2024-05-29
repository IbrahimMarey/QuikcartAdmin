package com.example.quikcartadmin.models.entities.products

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString(),
		parcel.readString(),
		parcel.readValue(Long::class.java.classLoader) as? Long,
		parcel.readString(),
		TODO("alt"),
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString(),
		TODO("variantIds"),
		parcel.readValue(Long::class.java.classLoader) as? Long,
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readValue(Int::class.java.classLoader) as? Int
	)
	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(updatedAt)
		parcel.writeString(src)
		parcel.writeValue(productId)
		parcel.writeString(adminGraphqlApiId)
		parcel.writeValue(width)
		parcel.writeString(createdAt)
		parcel.writeValue(id)
		parcel.writeValue(position)
		parcel.writeValue(height)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<ImagesItem> {
		override fun createFromParcel(parcel: Parcel): ImagesItem {
			return ImagesItem(parcel)
		}

		override fun newArray(size: Int): Array<ImagesItem?> {
			return arrayOfNulls(size)
		}
	}
}