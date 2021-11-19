package com.thequietz.travelog.di

import com.thequietz.travelog.api.GuideRecommendService
import com.thequietz.travelog.api.PlaceRecommendService
import com.thequietz.travelog.api.PlaceSearchService
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
    fun providePlaceRecommend(): GuideRecommendService {
        return GuideRecommendService.create()
    }

    @Singleton
    @Provides
    fun providePlaceService(): PlaceService {
        return PlaceService.create()
    }

    @Singleton
    @Provides
    fun providePlaceSearchService(): PlaceSearchService {
        return PlaceSearchService.create()
    }

    @Singleton
    @Provides
    fun providePlaceRecommendService(): PlaceRecommendService {
        return PlaceRecommendService.create()
    }
}
