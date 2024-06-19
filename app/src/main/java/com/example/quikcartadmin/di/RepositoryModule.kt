package com.example.quikcartadmin.di

import com.example.quikcartadmin.models.remote.datasource.CouponsDataSource
import com.example.quikcartadmin.models.repository.AdminRepository
import com.example.quikcartadmin.models.remote.datasource.RemoteDataSourceImp
import com.example.quikcartadmin.models.repository.IAdminRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun adminRepositoryProvider(
        remoteDataSource: RemoteDataSourceImp,
        couponsDataSource: CouponsDataSource
    ) : IAdminRepository{
        return AdminRepository(remoteDataSource, couponsDataSource)
    }
}