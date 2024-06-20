package com.example.quikcartadmin.model.repository

import com.example.quikcartadmin.model.remote.FakeCouponsRemoteDataSource
import com.example.quikcartadmin.model.remote.FakeRemoteDataSource
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
import com.example.quikcartadmin.models.remote.datasource.ICouponsRemoteDataSource
import com.example.quikcartadmin.models.remote.datasource.IRemoteDataSource
import com.example.quikcartadmin.models.repository.IAdminRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeAdminRepository : IAdminRepository {

    private val fakeCouponsDataSource: ICouponsRemoteDataSource = FakeCouponsRemoteDataSource()
    private val fakeRemoteDataSource: IRemoteDataSource = FakeRemoteDataSource()
    override suspend fun getCountOfProducts(): Flow<ProductsCountResponse> {
        return fakeRemoteDataSource.getCountOfProducts()
    }

    override suspend fun getCountOfCoupons(): Flow<CouponsCountResponse> {
        return fakeCouponsDataSource.getCountOfCoupons()
    }

    override suspend fun getCountOfInventory(): Flow<InventoryCountResponse> {
        return fakeRemoteDataSource.getCountOfInventory()
    }

    override suspend fun getAllProducts(): Flow<ProductsResponse> {
        return fakeRemoteDataSource.getAllProducts()
    }

    override suspend fun deleteProduct(productId: Long?) {
        fakeRemoteDataSource.deleteProduct(productId)
    }

    override suspend fun updateProduct(
        productId: Long,
        product: SingleProductsResponse
    ): SingleProductsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImageToProduct(
        productId: Long,
        imageBody: SingleImageBody
    ): SingleImageResponse {
        TODO("Not yet implemented")
    }

    override suspend fun createProduct(body: ProductBody): SingleProductsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getPriceRules(): Flow<List<PriceRule>> {
        return flowOf(fakeCouponsDataSource.getPriceRules())
    }

    override suspend fun getDiscounts(ruleID: Long): Flow<List<DiscountCode>> {
        return flowOf(fakeCouponsDataSource.getDiscounts(ruleID))
    }

    override suspend fun deleteDiscount(ruleID: Long, discountId: Long): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePriceRule(ruleID: Long): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDiscount(
        ruleID: Long,
        discountId: Long,
        body: DiscountCodeResponse
    ): Flow<DiscountCodeResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePriceRule(
        ruleID: Long,
        body: PriceRuleResponse
    ): Flow<PriceRuleResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createDiscountCode(
        ruleID: Long,
        body: DiscountCodeBody
    ): Flow<DiscountCode> {
        TODO("Not yet implemented")
    }

    override suspend fun createPriceRule(body: PriceRuleBody): Flow<PriceRuleResponse?> {
        TODO("Not yet implemented")
    }
}