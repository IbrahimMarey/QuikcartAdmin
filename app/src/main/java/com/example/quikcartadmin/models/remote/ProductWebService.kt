package com.example.quikcartadmin.models.remote

import com.example.quikcartadmin.models.entities.products_entities.ProductBody
import com.example.quikcartadmin.models.entities.products_entities.ProductsResponse
import com.example.quikcartadmin.models.entities.products_entities.SingleProductsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductWebServices {
    @GET("products.json?")
    suspend fun getProducts(): ProductsResponse

    @POST("products.json")
    suspend fun createProduct(@Body body: ProductBody): SingleProductsResponse
    @DELETE("products/{product_id}.json")
    suspend fun deleteProduct(
        @Path("product_id") productId : Long
    )

    @PUT("products/{product_id}.json")
    suspend fun updateProduct(
        @Path("product_id") productId : Long,
        @Body body: SingleProductsResponse
    ): SingleProductsResponse

}