package com.example.quikcartadmin.models.repository

import com.example.quikcartadmin.models.entities.coupons.CouponsCountResponse
import com.example.quikcartadmin.models.entities.inventory.InventoryCountResponse
import com.example.quikcartadmin.models.entities.products.ProductsCountResponse
import kotlinx.coroutines.flow.Flow

interface IAdminRepository {
    suspend fun getCountOfProducts() : Flow<ProductsCountResponse>
    suspend fun getCountOfCoupons() : Flow<CouponsCountResponse>
    suspend fun getCountOfInventory() : Flow<InventoryCountResponse>
}