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
import com.example.quikcartadmin.models.remote.datasource.CouponsDataSource
import com.example.quikcartadmin.models.remote.datasource.RemoteDataSourceImp
import com.example.quikcartadmin.models.repository.IAdminRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AdminRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImp,
    private val couponsDataSource: CouponsDataSource
) : IAdminRepository {
    override suspend fun getCountOfProducts(): Flow<ProductsCountResponse> {
        return remoteDataSource.getCountOfProducts()
    }

    override suspend fun getCountOfCoupons(): Flow<CouponsCountResponse> {
        return couponsDataSource.getCountOfCoupons()
    }

    override suspend fun getCountOfInventory(): Flow<InventoryCountResponse> {
        return remoteDataSource.getCountOfInventory()
    }

    override suspend fun getAllProducts(): Flow<ProductsResponse> {
        return remoteDataSource.getAllProducts()
    }
    override suspend fun deleteProduct(productId: Long?) {
        remoteDataSource.deleteProduct(productId)
    }

    override suspend fun updateProduct(
        productId: Long,
        product: SingleProductsResponse
    ): SingleProductsResponse {
        return remoteDataSource.updateProduct(productId,product)
    }

    override suspend fun uploadImageToProduct(
        productId: Long,
        imageBody: SingleImageBody
    ): SingleImageResponse {
        return remoteDataSource.uploadImageToProduct(productId, imageBody)
    }
    override suspend fun createProduct(body: ProductBody): SingleProductsResponse {
        return remoteDataSource.createProduct(body)
    }

    ///coupons
    override suspend fun getPriceRules(): Flow<List<PriceRule>> {
        return flowOf(couponsDataSource.getPriceRules())
    }

    override suspend fun getDiscounts(ruleID: Long): Flow<List<DiscountCode>> {
        return flowOf(couponsDataSource.getDiscounts(ruleID))
    }
    override suspend fun deleteDiscount(ruleID: Long, discountId: Long): Flow<String> {
        return flowOf(couponsDataSource.deleteDiscount(ruleID,discountId))
    }

    override suspend fun deletePriceRule(ruleID: Long): Flow<String> {
        return flowOf(couponsDataSource.deletePriceRule(ruleID))
    }

    override suspend fun updatePriceRule(ruleID: Long, body: PriceRuleResponse): Flow<PriceRuleResponse> {
        return flowOf(couponsDataSource.updatePriceRule(ruleID,body))
    }

    override suspend fun updateDiscount(
        ruleID: Long,
        discountId: Long,
        body: DiscountCodeResponse
    ): Flow<DiscountCodeResponse> {
        return flowOf(couponsDataSource.updateDiscount(ruleID,discountId,body))
    }

    override suspend fun createPriceRule(body: PriceRuleBody): Flow<PriceRuleResponse?> {
        return flowOf(couponsDataSource.createPriceRule(body))
    }

    override suspend fun createDiscountCode(ruleID : Long, body: DiscountCodeBody): Flow<DiscountCode> {
        return flowOf(couponsDataSource.createDiscountCode(ruleID, body))
    }

}