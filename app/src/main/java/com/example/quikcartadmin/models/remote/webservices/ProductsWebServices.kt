package com.example.quikcartadmin.models.remote.webservices

import com.example.quikcartadmin.BuildConfig
import com.example.quikcartadmin.models.entities.products.ProductBody
import com.example.quikcartadmin.models.entities.products.ProductsCountResponse
import com.example.quikcartadmin.models.entities.products.ProductsResponse
import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductsWebServices {
    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+ BuildConfig.PASSWORD)
    @GET("products/count.json")
    suspend fun getCountOfProducts(): ProductsCountResponse

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+ BuildConfig.PASSWORD)
    @GET("products.json")
    suspend fun getProducts(): ProductsResponse

    @POST("products.json")
    suspend fun createProduct(@Body body: ProductBody): SingleProductsResponse

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+ BuildConfig.PASSWORD)
    @DELETE("products/{product_id}.json")
    suspend fun deleteProduct(
        @Path("product_id") productId: Long?
    )

    @PUT("products/{product_id}.json")
    suspend fun updateProduct(
        @Path("product_id") productId : Long,
        @Body body: SingleProductsResponse
    ): SingleProductsResponse

}