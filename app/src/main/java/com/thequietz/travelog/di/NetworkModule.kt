package com.thequietz.travelog.di

import com.thequietz.travelog.api.PlaceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun providePlaceService(): PlaceService {
        return PlaceService.create()
    }
}
