package com.example.quikcartadmin.di

import com.example.quikcartadmin.helpers.Constants
import com.example.quikcartadmin.models.remote.webservices.CouponsWebServices
import com.example.quikcartadmin.models.remote.webservices.InventoryWebServices
import com.example.quikcartadmin.models.remote.webservices.ProductsWebServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkProvider {
    @Provides
    @Singleton
    fun provideOkHttp() : OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideProductsApiService(retrofit: Retrofit) : ProductsWebServices {
        return retrofit.create(ProductsWebServices::class.java)
    }
    @Provides
    @Singleton
    fun provideCouponsApiService(retrofit: Retrofit): CouponsWebServices {
        return retrofit.create(CouponsWebServices::class.java)
    }

    @Provides
    @Singleton
    fun provideInventoryApiService(retrofit: Retrofit): InventoryWebServices {
        return retrofit.create(InventoryWebServices::class.java)
    }
}