package com.example.quikcartadmin.models.remote.datasource

import com.example.quikcartadmin.models.entities.coupons.CouponsCountResponse
import com.example.quikcartadmin.models.entities.coupons.DiscountCode
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeBody
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeResponse
import com.example.quikcartadmin.models.entities.coupons.PriceRule
import com.example.quikcartadmin.models.entities.coupons.PriceRuleBody
import com.example.quikcartadmin.models.entities.coupons.PriceRuleResponse
import kotlinx.coroutines.flow.Flow

interface ICouponsRemoteDataSource {
    fun getCountOfCoupons(): Flow<CouponsCountResponse>
    suspend fun getDiscounts(ruleID: Long): List<DiscountCode>
    suspend fun getPriceRules(): List<PriceRule>
    suspend fun deleteDiscount(ruleID: Long, discountId: Long): String
    suspend fun deletePriceRule(ruleID: Long): String
    suspend fun updateDiscount(
        ruleID: Long,
        discountId: Long,
        body: DiscountCodeResponse
    ): DiscountCodeResponse

    suspend fun updatePriceRule(ruleID: Long, body: PriceRuleResponse): PriceRuleResponse
    suspend fun createDiscountCode(ruleID: Long, body: DiscountCodeBody): DiscountCode
    suspend fun createPriceRule(body: PriceRuleBody): PriceRuleResponse?
}