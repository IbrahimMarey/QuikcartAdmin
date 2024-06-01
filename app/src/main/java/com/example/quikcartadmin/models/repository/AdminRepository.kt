package com.example.quikcartadmin.models.repository

import com.example.quikcartadmin.models.entities.coupons.CouponsCountResponse
import com.example.quikcartadmin.models.entities.inventory.InventoryCountResponse
import com.example.quikcartadmin.models.entities.products.ProductBody
import com.example.quikcartadmin.models.entities.products.ProductsCountResponse
import com.example.quikcartadmin.models.entities.products.ProductsResponse
import com.example.quikcartadmin.models.entities.products.SingleImageBody
import com.example.quikcartadmin.models.entities.products.SingleImageResponse
import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import com.example.quikcartadmin.models.remote.datasource.RemoteDataSourceImp
import com.example.quikcartadmin.models.repository.IAdminRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdminRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImp
) : IAdminRepository {
    override suspend fun getCountOfProducts(): Flow<ProductsCountResponse> {
        return remoteDataSource.getCountOfProducts()
    }

    override suspend fun getCountOfCoupons(): Flow<CouponsCountResponse> {
        return remoteDataSource.getCountOfCoupons()
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
}