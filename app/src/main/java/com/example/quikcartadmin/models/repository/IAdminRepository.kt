package com.example.quikcartadmin.models.repository

import com.example.quikcartadmin.models.entities.coupons.CouponsCountResponse
import com.example.quikcartadmin.models.entities.coupons.DiscountCode
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeBody
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeResponse
import com.example.quikcartadmin.models.entities.coupons.PriceRule
import com.example.quikcartadmin.models.entities.coupons.PriceRuleBody
import com.example.quikcartadmin.models.entities.coupons.PriceRuleResponse
import com.example.quikcartadmin.models.entities.inventory.InventoryCountResponse
import com.example.quikcartadmin.models.entities.products.ProductBody
import com.example.quikcartadmin.models.entities.products.ProductsCountResponse
import com.example.quikcartadmin.models.entities.products.ProductsResponse
import com.example.quikcartadmin.models.entities.products.SingleImageBody
import com.example.quikcartadmin.models.entities.products.SingleImageResponse
import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import kotlinx.coroutines.flow.Flow

interface IAdminRepository {
    suspend fun getCountOfProducts() : Flow<ProductsCountResponse>
    suspend fun getCountOfCoupons() : Flow<CouponsCountResponse>
    suspend fun getCountOfInventory() : Flow<InventoryCountResponse>
    suspend fun getAllProducts() : Flow<ProductsResponse>
    suspend fun deleteProduct(productId: Long?)
    suspend fun updateProduct(productId: Long, product: SingleProductsResponse): SingleProductsResponse
    suspend fun uploadImageToProduct(productId: Long, imageBody: SingleImageBody): SingleImageResponse
    suspend fun createProduct(body: ProductBody): SingleProductsResponse


    suspend fun getPriceRules(): Flow<List<PriceRule>>
    suspend fun getDiscounts(ruleID: Long): Flow<List<DiscountCode>>
    suspend fun deleteDiscount(ruleID: Long, discountId: Long): Flow<String>
    suspend fun deletePriceRule(ruleID: Long): Flow<String>
    suspend fun updateDiscount(
        ruleID: Long,
        discountId: Long,
        body: DiscountCodeResponse
    ): Flow<DiscountCodeResponse>

    suspend fun updatePriceRule(ruleID: Long, body: PriceRuleResponse): Flow<PriceRuleResponse>
    suspend fun createDiscountCode(ruleID: Long, body: DiscountCodeBody): Flow<DiscountCode>
    suspend fun createPriceRule(body: PriceRuleBody): Flow<PriceRuleResponse?>
}
