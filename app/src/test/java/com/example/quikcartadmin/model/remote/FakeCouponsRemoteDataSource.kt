package com.example.quikcartadmin.model.remote

import com.example.quikcartadmin.models.entities.coupons.CouponsCountResponse
import com.example.quikcartadmin.models.entities.coupons.DiscountCode
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeBody
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeResponse
import com.example.quikcartadmin.models.entities.coupons.PriceRule
import com.example.quikcartadmin.models.entities.coupons.PriceRuleBody
import com.example.quikcartadmin.models.entities.coupons.PriceRuleResponse
import com.example.quikcartadmin.models.remote.datasource.ICouponsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeCouponsRemoteDataSource @Inject constructor():ICouponsRemoteDataSource {
    override fun getCountOfCoupons(): Flow<CouponsCountResponse> {
        return flowOf(CouponsCountResponse(count = 10))
    }

    override suspend fun getDiscounts(ruleID: Long): List<DiscountCode> {
        return listOf(DiscountCode(id = 1, code = "DISCOUNT10", priceRuleId = ruleID))
    }

    override suspend fun getPriceRules(): List<PriceRule> {
        return listOf(PriceRule(id = 1, title = "Summer Sale"))
    }

    override suspend fun deleteDiscount(ruleID: Long, discountId: Long): String {
        return "success"
    }

    override suspend fun deletePriceRule(ruleID: Long): String {
        return "success"
    }

    override suspend fun updateDiscount(
        ruleID: Long,
        discountId: Long,
        body: DiscountCodeResponse
    ): DiscountCodeResponse {
        return body
    }

    override suspend fun updatePriceRule(ruleID: Long, body: PriceRuleResponse): PriceRuleResponse {
        return body
    }

    override suspend fun createDiscountCode(ruleID: Long, body: DiscountCodeBody): DiscountCode {
        return DiscountCode(id = 2, code = "NEWCODE", priceRuleId = ruleID)
    }

    override suspend fun createPriceRule(body: PriceRuleBody): PriceRuleResponse? {
        return PriceRuleResponse(PriceRule(id = 2, title = "Winter Sale"))
    }
}