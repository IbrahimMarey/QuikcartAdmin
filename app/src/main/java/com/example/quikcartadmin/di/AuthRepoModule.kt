package com.example.quikcartadmin.di

import com.example.quikcartadmin.models.firbase.AuthenticationRepository
import com.example.quikcartadmin.models.firbase.IAuthenticationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepoModule {
    @Binds
    abstract fun provideAuthRepo(authRepoImpl: AuthenticationRepository): IAuthenticationRepository
}