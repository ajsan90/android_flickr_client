package com.awesome.flickrsearch.di.repos

import com.awesome.flickrsearch.components.PhotoInfoResult

interface ImageSearcher {
    suspend fun getPhotosByTag(tags: String, page: Int, numImagePerPage: Int, onPhotoUrlResults: (photoList: ArrayList<PhotoInfoResult>) -> Unit)
    fun cachePhotoResult(result: PhotoInfoResult, scrollIndex: Int)
    fun getCachedPhotoResult(): PhotoInfoResult?
    fun getCachedPhotoResultsList() : List<PhotoInfoResult>
    fun getCachedSearchTerms(): String

    fun getCachedScrollIndex(): Int
}