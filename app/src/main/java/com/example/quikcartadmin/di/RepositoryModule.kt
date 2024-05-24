package com.example.quikcartadmin.di

import AdminRepository
import com.example.quikcartadmin.models.remote.datasource.IRemoteDataSource
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
    fun adminRepositoryProvider(remoteDataSource: RemoteDataSourceImp
    ) : IAdminRepository{
        return AdminRepository(remoteDataSource)
    }
}