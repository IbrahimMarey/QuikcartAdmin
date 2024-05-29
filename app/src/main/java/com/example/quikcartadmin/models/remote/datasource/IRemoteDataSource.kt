package com.example.quikcartadmin.models.remote.datasource

import com.example.quikcartadmin.models.entities.coupons.CouponsCountResponse
import com.example.quikcartadmin.models.entities.inventory.InventoryCountResponse
import com.example.quikcartadmin.models.entities.products.ProductsCountResponse
import com.example.quikcartadmin.models.entities.products.ProductsResponse
import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import kotlinx.coroutines.flow.Flow

interface IRemoteDataSource {
    fun getCountOfProducts(): Flow<ProductsCountResponse>
    fun getCountOfCoupons(): Flow<CouponsCountResponse>
    fun getCountOfInventory(): Flow<InventoryCountResponse>
    fun getAllProducts(): Flow<ProductsResponse>
    suspend fun deleteProduct(productId: Long?)
    suspend fun updateProduct(productId: Long, product: SingleProductsResponse): SingleProductsResponse
}