package com.example.quikcartadmin.models.remote.datasource

import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import com.example.quikcartadmin.models.remote.webservices.CouponsWebServices
import com.example.quikcartadmin.models.remote.webservices.InventoryWebServices
import com.example.quikcartadmin.models.remote.webservices.ProductsWebServices
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDataSourceImp @Inject constructor(
    private val productService: ProductsWebServices,
    private val couponsWebServices: CouponsWebServices,
    private val inventoryWebServices: InventoryWebServices
    ): IRemoteDataSource {
    override fun getCountOfProducts() = flow {
        emit(productService.getCountOfProducts())
    }
    override fun getCountOfCoupons() = flow {
        emit(couponsWebServices.getCountOfCoupons())
    }
    override fun getCountOfInventory() = flow {
        emit(inventoryWebServices.getCountOfInventory())
    }

    override fun getAllProducts()= flow {
        emit(productService.getProducts())
    }
    override suspend fun deleteProduct(productId: Long?) {
        productService.deleteProduct(productId)
    }

    override suspend fun updateProduct(productId: Long, product: SingleProductsResponse): SingleProductsResponse {
        return productService.updateProduct(productId, product)
    }
}
