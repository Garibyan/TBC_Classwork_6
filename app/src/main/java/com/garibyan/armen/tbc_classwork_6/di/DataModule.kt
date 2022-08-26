package com.garibyan.armen.tbc_classwork_6.di

import android.content.Context
import com.garibyan.armen.tbc_classwork_6.repository.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) = DataStore(context)
}