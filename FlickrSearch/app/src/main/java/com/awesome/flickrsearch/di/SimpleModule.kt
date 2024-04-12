package com.awesome.flickrsearch.di

import FlickrWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SimpleModule  {
    @Provides
    @Singleton
    fun provideFlickrWrapper(): FlickrWrapper {
        return FlickrWrapper()
    }
}