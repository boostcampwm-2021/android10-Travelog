package com.thequietz.travelog.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun providesRepository(): Repository {
        return RepositoryImpl()
    }
}

interface Repository

class RepositoryImpl : Repository
