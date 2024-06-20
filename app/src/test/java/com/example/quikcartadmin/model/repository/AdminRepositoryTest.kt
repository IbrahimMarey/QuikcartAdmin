package com.example.quikcartadmin.model.repository

import com.example.quikcartadmin.model.remote.FakeCouponsRemoteDataSource
import com.example.quikcartadmin.model.remote.FakeRemoteDataSource
import com.example.quikcartadmin.models.entities.products.Product
import com.example.quikcartadmin.models.entities.products.ProductBody
import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import com.example.quikcartadmin.models.repository.AdminRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminRepositoryTest {

    private lateinit var adminRemoteDataSource: FakeRemoteDataSource
    private lateinit var couponsRemoteDataSource: FakeCouponsRemoteDataSource
    private lateinit var adminRepository: AdminRepository

    @Before
    fun setUp() {
        adminRemoteDataSource = FakeRemoteDataSource()
        couponsRemoteDataSource = FakeCouponsRemoteDataSource()
        adminRepository = AdminRepository(adminRemoteDataSource, couponsRemoteDataSource)
    }

    @Test
    fun testGetCountOfProducts() = runTest {
        val result = adminRepository.getCountOfProducts().first()
        assertEquals(2, result.count)
    }

    @Test
    fun testGetAllProducts() = runTest {
        val result = adminRepository.getAllProducts().first()
        assertEquals(2, result.products.size)
        assertEquals("Product 1", result.products[0].title)
        assertEquals("Product 2", result.products[1].title)
    }

    @Test
    fun testDeleteProduct() = runBlockingTest {
        val productId = adminRemoteDataSource.fakeProducts[0].id
        adminRepository.deleteProduct(productId)
        val result = adminRepository.getAllProducts().first()
        assertEquals(1, result.products.size)
        assertEquals("Product 2", result.products[0].title)
    }

    @Test
    fun testUpdateProduct() = runBlockingTest {
        val updatedProduct = SingleProductsResponse(
            product = Product(id = adminRemoteDataSource.fakeProducts[0].id, title = "Updated Product", bodyHtml = "Updated Description")
        )
        val productId = adminRemoteDataSource.fakeProducts[0].id!!

        val result = adminRepository.updateProduct(productId, updatedProduct)

        assertEquals(updatedProduct.product.id, result.product.id)
        assertEquals(updatedProduct.product.title, result.product.title)
        assertEquals(updatedProduct.product.bodyHtml, result.product.bodyHtml)

        val allProducts = adminRepository.getAllProducts().first()
        assertEquals("Updated Product", allProducts.products[0].title)
        assertEquals("Updated Description", allProducts.products[0].bodyHtml)
    }

    @Test
    fun testCreateProduct() = runBlockingTest {
        // Given
        val productBody = ProductBody(product = Product(title ="New Product"))

        // When
        val result = adminRepository.createProduct(productBody)

        // Then
        assertEquals("New Product", result.product.title)
    }
}
