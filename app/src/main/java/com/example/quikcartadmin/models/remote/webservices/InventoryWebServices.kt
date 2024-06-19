package com.example.quikcartadmin.models.remote.webservices

import com.example.quikcartadmin.BuildConfig
import com.example.quikcartadmin.models.entities.coupons.CouponsCountResponse
import com.example.quikcartadmin.models.entities.inventory.InventoryCountResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface InventoryWebServices {

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+ BuildConfig.PASSWORD)
    @GET("locations/count.json")
    suspend fun getCountOfInventory(): InventoryCountResponse

}