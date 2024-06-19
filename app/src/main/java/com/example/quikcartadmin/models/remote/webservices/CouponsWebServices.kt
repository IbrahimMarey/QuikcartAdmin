package com.example.quikcartadmin.models.remote.webservices

import com.example.quikcartadmin.BuildConfig
import com.example.quikcartadmin.models.entities.coupons.CouponsCountResponse
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeBody
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeModel
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeResponse
import com.example.quikcartadmin.models.entities.coupons.PriceRuleBody
import com.example.quikcartadmin.models.entities.coupons.PriceRuleModel
import com.example.quikcartadmin.models.entities.coupons.PriceRuleResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CouponsWebServices {
    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+ BuildConfig.PASSWORD)
    @GET("discount_codes/count.json")
    suspend fun getCountOfCoupons(): CouponsCountResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:"+ BuildConfig.PASSWORD)
    @POST("price_rules.json")
    suspend fun createPriceRule(
        @Body body : PriceRuleBody
    ): PriceRuleResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:"+BuildConfig.PASSWORD)
    @POST("price_rules/{price_rule_id}/discount_codes.json")
    suspend fun createDiscountCode(
        @Path("price_rule_id") ruleID : Long,
        @Body body: DiscountCodeBody
    ): DiscountCodeResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:"+BuildConfig.PASSWORD)
    @GET("price_rules.json?")
    suspend fun getPriceRules(): PriceRuleModel

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:"+BuildConfig.PASSWORD)
    @GET("price_rules/{price_rule_id}/discount_codes.json")
    suspend fun getDiscounts(@Path("price_rule_id") ruleID : Long): DiscountCodeModel

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:"+BuildConfig.PASSWORD)
    @PUT("price_rules/{price_rule_id}/discount_codes/{discount_code_id}.json")
    suspend fun updateDiscount(
        @Path("price_rule_id") ruleID : Long,
        @Path("discount_code_id") discountId : Long,
        @Body body: DiscountCodeResponse
    ): DiscountCodeResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:"+BuildConfig.PASSWORD)
    @DELETE("price_rules/{price_rule_id}/discount_codes/{discount_code_id}.json")
    suspend fun deleteDiscount(
        @Path("price_rule_id") ruleID : Long,
        @Path("discount_code_id") discountId : Long,
    )

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:"+BuildConfig.PASSWORD)
    @PUT("price_rules/{price_rule_id}.json")
    suspend fun updatePriceRule(
        @Path("price_rule_id") ruleID : Long,
        @Body body: PriceRuleResponse
    ): PriceRuleResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:"+BuildConfig.PASSWORD)
    @DELETE("price_rules/{price_rule_id}.json")
    suspend fun deletePriceRule(
        @Path("price_rule_id") ruleID : Long
    )


}