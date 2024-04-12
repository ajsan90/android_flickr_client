package com.awesome.flickrsearch.di

import com.awesome.flickrsearch.components.FlickrWrapper
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
        return object : ImageSearcher {
            val flickerWrapper = FlickrWrapper()
        }
    }

    interface ImageSearcher {

    }
}
