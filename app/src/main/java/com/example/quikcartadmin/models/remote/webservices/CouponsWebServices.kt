package com.example.quikcartadmin.models.remote.webservices

import com.example.quikcartadmin.BuildConfig
import com.example.quikcartadmin.models.entities.coupons.CouponsCountResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface CouponsWebServices {
    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+ BuildConfig.PASSWORD)
    @GET("discount_codes/count.json")
    suspend fun getCountOfCoupons(): CouponsCountResponse

}