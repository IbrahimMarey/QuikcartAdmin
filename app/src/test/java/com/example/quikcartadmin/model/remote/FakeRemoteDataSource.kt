package com.example.quikcartadmin.model.remote

import com.example.quikcartadmin.models.entities.inventory.InventoryCountResponse
import com.example.quikcartadmin.models.entities.products.*
import com.example.quikcartadmin.models.remote.datasource.IRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeRemoteDataSource@Inject constructor() : IRemoteDataSource {

    val fakeProducts = mutableListOf(
        ProductsItem(id = 1, title = "Product 1", bodyHtml = "Description 1"),
        ProductsItem(id = 2, title = "Product 2", bodyHtml = "Description 2")
    )
    override fun getCountOfProducts(): Flow<ProductsCountResponse> {
        return flow {
            emit(ProductsCountResponse(fakeProducts.size))
        }
    }

    override fun getCountOfInventory(): Flow<InventoryCountResponse> {
        return flow {
            emit(InventoryCountResponse(200))
        }
    }

    override fun getAllProducts(): Flow<ProductsResponse> {
        return flow {
            emit(ProductsResponse(fakeProducts))
        }
    }

    override suspend fun deleteProduct(productId: Long?) {
        fakeProducts.removeIf { it.id == productId }
    }

    override suspend fun updateProduct(productId: Long, product: SingleProductsResponse): SingleProductsResponse {
        val index = fakeProducts.indexOfFirst { it.id == productId }
        if (index != -1) {
            fakeProducts[index] = ProductsItem(id = productId, title = product.product.title, bodyHtml = product.product.bodyHtml)
        }
        return product
    }

    override suspend fun uploadImageToProduct(productId: Long, imageBody: SingleImageBody): SingleImageResponse {
        return SingleImageResponse(SingleImageRes(productId = productId, src = imageBody.image?.src))
    }

    override suspend fun createProduct(body: ProductBody): SingleProductsResponse {
        // fakeProducts.add(body)
        return SingleProductsResponse(Product(id = body.product?.id, title = body.product?.title, bodyHtml = body.product?.bodyHtml))
    }
}
