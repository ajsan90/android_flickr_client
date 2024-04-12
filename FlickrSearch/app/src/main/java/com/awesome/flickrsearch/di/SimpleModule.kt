package com.awesome.flickrsearch.di

import com.awesome.flickrsearch.components.FlickrWrapper
import com.awesome.flickrsearch.components.PhotoInfoResult
import com.awesome.flickrsearch.di.repos.ImageSearcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SimpleModule  {
    @Singleton
    @Provides
    fun provideFlickrWrapper(): ImageSearcher {
        return FlickrWrapper()
    }
}
