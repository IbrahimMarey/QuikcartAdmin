package com.example.quikcartadmin.models.remote.datasource

import com.example.quikcartadmin.models.entities.coupons.DiscountCode
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeBody
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeResponse
import com.example.quikcartadmin.models.entities.coupons.PriceRule
import com.example.quikcartadmin.models.entities.coupons.PriceRuleBody
import com.example.quikcartadmin.models.entities.coupons.PriceRuleResponse
import com.example.quikcartadmin.models.remote.webservices.CouponsWebServices
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CouponsDataSource @Inject constructor(
    private val couponsWebServices: CouponsWebServices,
    ) : ICouponsRemoteDataSource {
    override fun getCountOfCoupons() = flow {
        emit(couponsWebServices.getCountOfCoupons())
    }
    override suspend fun getPriceRules(): List<PriceRule>{
        return couponsWebServices.getPriceRules().priceRules
    }
    override suspend fun getDiscounts(ruleID: Long): List<DiscountCode> {
        return couponsWebServices.getDiscounts(ruleID).discount_codes
    }

    override suspend fun deleteDiscount(ruleID: Long, discountId: Long) : String{
        return try {
            couponsWebServices.deleteDiscount(ruleID, discountId)
            "success"
        }catch (ex : Exception){
            "error"
        }
    }

    override suspend fun deletePriceRule(ruleID: Long): String {
        return try {
            couponsWebServices.deletePriceRule(ruleID)
            "success"
        }catch (ex : Exception){
            "error"
        }
    }

    override suspend fun createPriceRule(body : PriceRuleBody): PriceRuleResponse? {
        return couponsWebServices.createPriceRule(body)
    }

    override suspend fun createDiscountCode(ruleID : Long, body : DiscountCodeBody,): DiscountCode {
        return couponsWebServices.createDiscountCode(ruleID,body).discountCode
    }

    override suspend fun updatePriceRule(ruleID: Long, body: PriceRuleResponse): PriceRuleResponse {
        return couponsWebServices.updatePriceRule(ruleID,body)
    }

    override suspend fun updateDiscount(
        ruleID: Long,
        discountId: Long,
        body: DiscountCodeResponse
    ): DiscountCodeResponse {
        return couponsWebServices.updateDiscount(ruleID,discountId,body)
    }
}