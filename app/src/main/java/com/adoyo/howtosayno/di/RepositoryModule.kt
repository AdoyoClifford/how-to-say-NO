package com.adoyo.howtosayno.di

import com.adoyo.howtosayno.data.repository.NoReasonRepository
import com.adoyo.howtosayno.data.repository.NoReasonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing repository dependencies
 * Binds repository interfaces to their concrete implementations
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    /**
     * Binds NoReasonRepository interface to its implementation
     */
    @Binds
    @Singleton
    abstract fun bindNoReasonRepository(
        noReasonRepositoryImpl: NoReasonRepositoryImpl
    ): NoReasonRepository
}